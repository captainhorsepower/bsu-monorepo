import curses
from enum import Enum
from os import remove, stat
import yaml  # PyYaml docs: https://pyyaml.org/wiki/PyYAMLDocumentation
import random


def main():
    rules, target = loadRules('sample-rules.yaml')
    target, targetOptions = chooseTarget(rules, default=target)
    ansDict = resolveAns(rules, target)
    drawResult(target, ansDict, fallbackOpts=targetOptions + ['Просто троллю'])


def loadRules(filename):
    yamlFile = yaml.load(open(filename), Loader=yaml.FullLoader)
    yamlRules = yamlFile['rules']
    return [Rule(yamlRule) for yamlRule in yamlRules], yamlFile['default-target']


def chooseTarget(rules, default):
    thenDict = {}
    for r in rules:
        thenDict.update(r.getThen())

    def _targetOptions(k):
        return list(set(sum([r.targetOptions(k) for r in rules], start=[])))

    def _optStr(k):
        prefix = '[default]' if k == default else '[test]'
        return f"{prefix:10}{k:15}{_targetOptions(k)}"

    invOpts = {_optStr(k): k for k in thenDict}
    target = invOpts[ask('Что угадываем? Справа список выводимых правилами ответов', list(invOpts.keys()))]
    return target, _targetOptions(target)


def resolveAns(rules, target, context=None):
    context = context if context is not None else dict()
    random.shuffle(rules)  # shuffle rules for even more fun!
    for r in [r for r in rules if r.canAnswer(target)]:
        val = _resolveRuleAns(r, context, rules)
        if (val):
            _drawRule(r)
            return val


def drawResult(target, ansDict, fallbackOpts):
    def _draw(stdscr):
        stdscr.erase()
        if ansDict:
            stdscr.addstr(f"Спасибо за игру!\n\n")
            stdscr.addstr(f"Получилось: {target} = {ansDict[target]}\n")
        else:
            stdscr.addstr(
                f"Все известные правила для '{target}' оказались ложью.\n\n")
            ask(question="Что вы загадали?",
                options=fallbackOpts)

        stdscr.getch()

    curses.wrapper(_draw)
    

def _resolveRuleAns(rule, context, rules):
    status, val = rule.when(context)
    while(status == RuleState.UNKNOWN):
        _resolveKeyToContext(key=val, rules=rules, context=context)
        status, val = rule.when(context)
        print((status, val))

    rules.remove(rule)
    return val


def _resolveKeyToContext(key, rules, context):
    for r in [r for r in rules if r.canAnswer(key)]:
        ansDict = _resolveRuleAns(r, context, rules)
        if (ansDict):
            context.update(ansDict)
            return

    val = ask(question=f"Выберите '{key}':",
              # can keep ref to parent rules to get options only from
              # 'helpful' rules and always win! Rly?
              options=list(set(sum([r.options(key)
                                    for r in rules], start=[]))),
              postHooks=[_drawMessageHook(f'\nОсталось правил: {len(rules)}'),
                         _drawContextHook(context)])
    context[key] = val


def _drawContextHook(context):
    def _hook(stdscr):
        stdscr.addstr('\nТекущий контекст:\n')
        for k in context:
            stdscr.addstr(f'\t{k:20}{context[k]}\n')

    return _hook


def _drawMessageHook(message):
    def _hook(stdscr):
        stdscr.addstr(f'{message}\n')

    return _hook


def _drawRule(rule):
    def _draw(stdscr):
        stdscr.erase()
        stdscr.addstr(f"По правилу:\n", curses.A_UNDERLINE)
        stdscr.addstr(f"если:\n")
        d = rule.getWhen()
        for k in d:
            stdscr.addstr(f'\t{k:20}{d[k]}\n')
        stdscr.addstr(f"то:\n")
        d = rule.getThen()
        for k in d:
            stdscr.addstr(f'\t{k:20}{d[k]}\n')
        stdscr.getch()

    curses.wrapper(_draw)


RuleState = Enum('RuleState', 'FAIL SUCCESS UNKNOWN')


class Rule:
    """
    - getWhen = dict of requirements
    - given = current context
    - getThen = result if requirements satisfied
    """

    def __init__(self, yamlRule):
        """
        Supported yaml structure:
        when: # условия для выполнения правила
          and:  # list of key-value pairs, all must match. 
                # or:... is not no supported cuz not required.
          - key1: val1 #
        then: 
        - res-key1: res-val1
        """
        self.__when = dict()
        for r in yamlRule['when']['and']:
            self.__when.update(r)

        self.__then = dict()
        for t in yamlRule['then']:
            self.__then.update(t)

    def __repr__(self) -> str:
        return str(self)

    def __str__(self) -> str:
        return f'''
        when: {self.__when} 
        then: {self.__then}
        '''

    def getWhen(self):
        return self.__when

    def getThen(self):
        return self.__then

    def canAnswer(self, target):
        return target in self.__then

    def when(self, context):
        for k, v in self.__when.items():
            vv = context.get(k, None)
            if not vv:
                return RuleState.UNKNOWN, k
            elif v != vv:
                return RuleState.FAIL, None

        return RuleState.SUCCESS, self.__then

    def options(self, key):
        v = self.__when.get(key, None)
        return [v] if v else []

    def targetOptions(self, key):
        v = self.__then.get(key, None)
        return [v] if v else []


def ask(question, options, preHooks=None, postHooks=None):
    choice = ''

    def _do_multiple_choice(stdscr):
        attributes = {}
        curses.init_pair(1, curses.COLOR_WHITE, curses.COLOR_BLACK)
        attributes['normal'] = curses.color_pair(1)

        curses.init_pair(2, curses.COLOR_BLACK, curses.COLOR_WHITE)
        attributes['highlighted'] = curses.color_pair(2)

        c = 0  # last character read
        option = 0  # the current option that is marked
        while c != 10:  # Enter in ascii
            stdscr.erase()
            _drawHooks(stdscr, preHooks)

            stdscr.addstr(f"{question}\n", curses.A_UNDERLINE)
            for i in range(len(options)):
                attr = attributes['highlighted'] if i == option else attributes['normal']
                stdscr.addstr(f"{i + 1}. ")
                stdscr.addstr(options[i] + '\n', attr)

            _drawHooks(stdscr, postHooks)

            c = stdscr.getch()
            if 1 <= c - ord('0') <= len(options):
                option = c - ord('0') - 1
            elif c == curses.KEY_UP and option > 0:
                option -= 1
            elif c == curses.KEY_DOWN and option < len(options) - 1:
                option += 1

        nonlocal choice
        choice = options[option]
        stdscr.erase()
        stdscr.addstr(f"Ваш выбор: {choice}")
        stdscr.getch()

    curses.wrapper(_do_multiple_choice)
    return choice


def _drawHooks(stdcrs, hooks):
    if hooks:
        for h in hooks:
            h(stdcrs)


if __name__ == "__main__":
    main()
