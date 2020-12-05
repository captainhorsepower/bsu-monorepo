import curses
from enum import Enum
import yaml  # PyYaml docs: https://pyyaml.org/wiki/PyYAMLDocumentation


def main():
    rules = loadRules('sample-rules.yaml')
    print(rules)


def loadRules(filename):
    yamlFile = open(filename)
    yamlRules = yaml.load(yamlFile, Loader=yaml.FullLoader)['rules']
    return [Rule(yamlRule) for yamlRule in yamlRules]


class Rule:
    """
    - __when = dict of requirements
    - given = current context
    - __then = result if requirements satisfied
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

    # def test(self, context):
    #     for p in self.__predicates:
    #         if p.test(context) == RuleState.FAIL:
    #             return RuleState

# RuleState = Enum('RuleState', 'FAIL SUCCESS UNKNOWN')


def ask(question, options):
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
            stdscr.addstr(f"{question}\n", curses.A_UNDERLINE)
            for i in range(len(classes)):
                attr = attributes['highlighted'] if i == option else attributes['normal']
                stdscr.addstr(f"{i + 1}. ")
                stdscr.addstr(classes[i] + '\n', attr)

            c = stdscr.getch()
            if 1 <= c - ord('0') <= len(options):
                option = c - ord('0') - 1
            elif c == curses.KEY_UP and option > 0:
                option -= 1
            elif c == curses.KEY_DOWN and option < len(options) - 1:
                option += 1

        nonlocal choice
        choice = options[option]
        stdscr.addstr(f"Ваш выбор: {choice}")
        stdscr.getch()

    curses.wrapper(_do_multiple_choice)
    return choice


if __name__ == "__main__":
    main()
