# sample ASCII ui from stackoverflow
# UI todo: https://stackoverflow.com/questions/11284748/how-do-i-create-a-static-framed-ascii-interface-in-python
import curses


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


classes = ["The sneaky thief", "The smarty wizard", "The proletariat"]

choice_outer = ask('What is your class?', classes)

print(choice_outer)
