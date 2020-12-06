import curses
from enum import Enum
from os import remove, stat
import yaml  # PyYaml docs: https://pyyaml.org/wiki/PyYAMLDocumentation
import random
from glob import glob as findFiles


def main():
    filename = chooseDatabaseFile()
    rules, target = chooseRules(filename)
    target, targetOptions = chooseTarget(rules, default=target)

    akinator = Akinator(rules, target)
    ansDict = akinator.solveAkinator()

    AsciiUtil.drawResult(target=target,
                         ansDict=ansDict,
                         fallbackOpts=targetOptions + ['Просто троллю'],
                         asked=akinator.getAsked(), guessed=akinator.getGuessed())


def chooseDatabaseFile():
    """
    1. get .yaml files
    2. remove files wihtout 'rules' and 'name'
    3. ask which file to use
    4. return filename
    """
    dotYamls = list(set(findFiles("**/*.yaml") + findFiles("*.yaml")))

    def _fileOptStr(filename):
        y = yaml.load(open(filename), Loader=yaml.FullLoader)
        name = y.get('name', '<no name>')
        return f"{name:15}[{filename}]" if 'rules' in y else None

    d = {_fileOptStr(f): f for f in dotYamls if (_fileOptStr(f))}
    костыль = 'Выход.'
    c = AsciiUtil.ask(question="Выберите базу:",
                      options=list(d.keys()) + [костыль],
                      preHooks=[AsciiUtil.drawMessageHook(
                          """
Добро пожаловать в ASCII акинатор.

Сделал Воробей Артём, 4гр.
"""
                      )],
                      postHooks=[AsciiUtil.drawMessageHook(f"\nОбнаруженные **.*.yaml: {dotYamls}\n"),
                                 AsciiUtil.drawMessageHook(
                          """
База знаний хранится в .yaml файлах со структурой:
---
name: <имя базы>
default-target: <предусмотренная разработчиком базы цель>
rules:
- when: # условия для выполнения правила
- then: # резльтирующие признаки

Сделал Воробей Артём, 4гр.
""")])
    if c == костыль:
        exit()
    return d[c]


def chooseRules(filename):
    yamlFile = yaml.load(open(filename), Loader=yaml.FullLoader)
    yamlRules = yamlFile['rules']
    return [Rule(yamlRule) for yamlRule in yamlRules], yamlFile.get('default-target', None)


def chooseTarget(rules, default):
    thenDict = {}
    for r in rules:
        thenDict.update(r.getThen())

    def _targetOptions(k):
        return list(set(sum([r.targetOptions(k) for r in rules], start=[])))

    def _optStr(k):
        prefix = '[default]' if k == default else '[test]'

        def formatLongList(l):
            MAX_LINE_SYMBOLS = 50
            inds = [0]
            for curr in range(len(l)):
                prev = inds[-1]
                if (curr == prev):
                    continue
                if len(str(l[prev:(curr+1)])) > MAX_LINE_SYMBOLS:
                    inds.append(curr)
            inds.append(len(l))

            lines = []
            for i in range(len(inds) - 1):
                lines.append(l[inds[i]:inds[i+1]])

            firstLine = ', '.join(lines[0])
            otherLines = '' if len(lines) == 1 else \
                ',\n' + \
                ',\n'.join(
                    map(lambda l: f'{"":30}{l}', map(', '.join, lines[1:])))
            return f"{{ {firstLine}{otherLines} }}"

        return f"{prefix:10}{k:15}{formatLongList(_targetOptions(k))}"

    invOpts = {_optStr(k): k for k in thenDict}
    target = invOpts[AsciiUtil.ask(
        'Что угадываем? Справа список выводимых правилами ответов', list(invOpts.keys()))]
    return target, _targetOptions(target)


class Akinator:

    def __init__(self, rules, target):
        self.rules = rules
        random.shuffle(self.rules)
        self.finalTarget = target
        self.context = {}
        self.asked = 0
        print(self.rules)
        print(self.finalTarget)

    def solveAkinator(self):
        while True:
            targetRules = [r for r in self.rules if r.canAnswer(self.finalTarget)]
            if not targetRules:
                return
            for r in targetRules:
                _, val = self.__resolveRule(r)
                if val:
                    print(val)
                    return val

    def __resolveRule(self, rule):
        while True:
            status, val = rule.when(self.context)
            if status == RuleState.UNKNOWN:
                self.__resolveKeyToContext(key=val)
            else:
                if val:
                    AsciiUtil.drawRule(rule)
                return status, val

    def __resolveKeyToContext(self, key):
        val = None

        # try to resolve value with rules
        for r in [r for r in self.rules if r.canAnswer(key)]:
            _, val = self.__resolveRule(r)
            if val:
                break

        # else aks user
        if not val:
            self.asked += 1
            val = AsciiUtil.ask(
                question=f"Выберите '{key}':",
                options=list(set(sum([r.options(key)
                                      for r in self.rules
                                      if not r.canAnswer(key)],
                                     start=[]))),
                postHooks=[AsciiUtil.drawContextHook(self.context)])
        else:
            val = val[key]

        self.__updContext(key, val)

    def __updContext(self, key, val):
        self.context[key] = val
        
        # remove failing rules
        for r in [r for r in self.rules if r.when(self.context)[0] == RuleState.FAIL]:
            self.rules.remove(r)

        print('-----------------------------')
        print(f'put {key}: {val} to contex')
        print(f'context: {len(self.context)} {self.context}') 
        print(f'rules: {len(self.rules)} {self.rules}') 

    def getAsked(self):
        return self.asked

    def getGuessed(self):
        return len(self.context) - self.asked


class AsciiUtil:
    def ask(question, options, preHooks=None, postHooks=None):
        choice = None

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
                AsciiUtil.__drawHooks(stdscr, preHooks)

                stdscr.addstr(f"{question}\n", curses.A_UNDERLINE)
                for i in range(len(options)):
                    attr = attributes['highlighted'] if i == option else attributes['normal']
                    stdscr.addstr(f"{i + 1}. ")
                    stdscr.addstr(options[i] + '\n', attr)

                stdscr.addstr('\n')
                AsciiUtil.__drawHooks(stdscr, postHooks)

                c = stdscr.getch()
                if 1 <= c - ord('0') <= len(options):
                    option = c - ord('0') - 1
                elif c == curses.KEY_UP and option > 0:
                    option -= 1
                elif c == curses.KEY_DOWN and option < len(options) - 1:
                    option += 1

            nonlocal choice
            choice = options[option]

        curses.wrapper(_do_multiple_choice)
        return choice

    def __drawHooks(stdcrs, hooks):
        if hooks:
            for h in hooks:
                h(stdcrs)

    def drawMessageHook(message):
        def _hook(stdscr):
            stdscr.addstr(f'{message}\n')
        return _hook

    def drawContextHook(context):
        def _hook(stdscr):
            stdscr.addstr('\nТекущий контекст:\n')
            for k in context:
                stdscr.addstr(f'\t{k:20}{context[k]}\n')
        return _hook

    def drawRule(rule):
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

    def drawResult(target, ansDict, fallbackOpts, asked, guessed):
        def _draw(stdscr):
            stdscr.erase()
            if ansDict:
                stdscr.addstr(f"Спасибо за игру!\n\n")
                stdscr.addstr(f"Получилось: {target} = {ansDict[target]}\n")
                stdscr.addstr(
                    f"\nАкинатор задал {asked} вопрос(ов|а)\nи вывел {guessed} значени(й|я)\n")
            else:
                AsciiUtil.ask(question="Что вы загадали?",
                              options=fallbackOpts,
                              preHooks=[AsciiUtil.drawMessageHook(f"Все известные правила для '{target}' оказались ложью.\n\n")])

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


if __name__ == "__main__":
    main()
