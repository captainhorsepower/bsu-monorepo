#!python3
# -*- coding: UTF-8 -*-

from functools import reduce
import csv
import curses

print('✓', '☑', '☐', '☒')

class BClassifier(object):
    """
    Simple classification by boolean attrs only
    as described in laba.
    """
    
    def __init__(self, items) -> None:
        """
        item = csv row = clazz_name, item_name, *attrs
        """
        
        self.init_train_data(items)
        
        self.attrs_len = len(items[0][2:])
    
    
    def init_train_data(self, items):
        self.clazzes = {}
        # fill clazz with train items
        for i in items:
            clazz_name = i[0]
            item_name = i[1]
            attrs = i[2:]
            
            if clazz_name not in self.clazzes:
                self.clazzes[clazz_name] = {'items': {}}
              
            clazz = self.clazzes[clazz_name]
            clazz['items'][item_name] = attrs
       
    
    def t_sum(t, lists):
        """sum of t-s item in every list in lists"""
        return reduce(lambda total, arr: total + arr[t], lists, 0)
            
    def train(self):
        for clazz in self.clazzes.values():
            clazz['b'] = []
            for t in range(self.attrs_len):
                r = BClassifier.t_sum(t, clazz['items'].values())
                clazz['b'].append(r / len(clazz['items']))

        b_avg = []
        for t in range(self.attrs_len):
            bs = map(lambda c: c['b'], self.clazzes.values())
            b_t = BClassifier.t_sum(t, bs) / len(self.clazzes)
            b_avg.append(b_t)


        for clazz in self.clazzes.values():
            clazz['a'] = []
            for t in range(self.attrs_len):
                a = abs(clazz['b'][t] - b_avg[t])
                clazz['a'].append(a)
                
                
    def muVect(self, x_attrs) -> dict:
        """x_attrs is csv-like record, same as item[2:]"""
        
        clazzMu = {}
        for cname, clazz in self.clazzes.items():
            a = clazz['a']
            
            def mu(item, x):
                suda = sum(map(lambda t: 
                               (1 if item[t] == x[t] else -1) 
                               * a[t], 
                               range(self.attrs_len)))
                return max(0, suda / sum(a))
            
            clazzMu[cname] = max([mu(item, x_attrs) for item in clazz['items'].values()])
            
        return clazzMu
    
    
    def interpret(self, muVect):
        best = max(muVect.items(), key=lambda t: t[-1])[0]
        return {
            'mu_vect': muVect,
            'best': best,
            'best_items': list(self.clazzes[best]['items'].keys())
        }

    
class FrontEndApp:
    """
    hold and manage app state, create ASCII interface
    """
    
    def __init__(self, f):
        headings, items = FrontEndApp.read_csv(f)
        self.filename = f
        self.clazz_attr_name = headings[0]
        self.attr_names = headings[2:]
        
        self.clf = BClassifier(items)
        self.clf.train()

        FrontEndApp.init_ascii()        


    def init_ascii():
        w=curses.initscr()
        w.scrollok(1) # enable scrolling
        # w.timeout(1)  # make 1-millisecond timeouts on `getch`    
            
    def read_csv(f):
        with open(f, 'r') as csvfile:
            items = []

            reader = csv.reader(csvfile, quotechar='|')
            for i, row in enumerate(reader):
                if i == 0:
                    headings = row
                    continue
                clazz_name, item_name, attrs = row[0], row[1], list(map(int, row[2:]))
                items.append((clazz_name, item_name, *attrs))
        return headings, items
    

    def greetings(self):
        def do_greetings(stdscr):
            stdscr.erase()
            stdscr.addstr("Welcome to Laba 2.\n")
            stdscr.addstr("Сделал Воробей Артём, 4группа.\n")
            stdscr.addstr(f"\nData File: {self.filename}\n")
            stdscr.addstr("\nPress eny key to continue...\n", curses.A_UNDERLINE)
            stdscr.getch()
        curses.wrapper(do_greetings)
    
    def select_attributes(self):
        selected = None

        def do_multiple_choice(stdscr):
            curses.init_pair(1, curses.COLOR_WHITE, curses.COLOR_BLACK)
            curses.init_pair(2, curses.COLOR_BLACK, curses.COLOR_WHITE)  
            attributes = {
                'normal': curses.color_pair(1),
                'highlighted': curses.color_pair(2)
            }
            
            options = self.attr_names + ['xxxxx  Done!  xxxxx']
            nonlocal selected
            selected = [False for _ in self.attr_names]

            option = 0  # the current option that is marked
            while True:  # Enter in ascii
                stdscr.erase()
                stdscr.addstr("Select features! ☑ => selected, ☐ => not selected\n\n", curses.A_UNDERLINE)
    
                for i in range(len(options)):
                    attr = attributes['highlighted'] if i == option else attributes['normal']
    
                    if i != len(options) - 1:
                        pref = "{:2d}. {:1s} ".format(i + 1, '☑' if selected[i] else '☐')
                        stdscr.addstr(pref)
                    stdscr.addstr(f'{options[i]}\n', attr)

                stdscr.addstr("\nMake sure your terminal is tall enough.\n", curses.A_UNDERLINE)
                stdscr.addstr("If you don't see the first line (without index) or window is just buggy, resize terminal window (yeah, nice fix)\n", curses.A_UNDERLINE)

                c = stdscr.getch()
                if c == 10:
                    if option == len(options) - 1:
                        break

                    selected[option] = not selected[option]

                elif c == curses.KEY_UP:
                    option = (option if option > 0 else len(options)) - 1
                elif c == curses.KEY_DOWN and option < len(options) - 1:
                    option = option + 1 if option < len(options) - 1 else 0

        curses.wrapper(do_multiple_choice)
        return list(map(int, selected))
        

    def display_best_match(self, features):
        muVect = self.clf.muVect(features)
        defaultIterpr = self.clf.interpret(muVect)

        def do_interpr(stdscr):
            stdscr.erase()
            stdscr.addstr("Done, matches found.\n\n")
            stdscr.addstr(f"Closest {self.clazz_attr_name} is {defaultIterpr['best']}.\n")
            stdscr.addstr(f"Some items in this class: { defaultIterpr['best_items']}.\n\n")

            stdscr.addstr("Your 'best match' vector looked like:\n")
            for clazz, score in defaultIterpr['mu_vect'].items():
                line = '{:10s}{:20s}: {:.5f}\n'.format('', clazz, score)
                stdscr.addstr(line)

            stdscr.addstr("\n\nPress eny key to exit...\n", curses.A_UNDERLINE)
            stdscr.getch()
        curses.wrapper(do_interpr)  

if __name__ == "__main__":

    app = FrontEndApp('games.csv')
    app.greetings()
    features = app.select_attributes()
    app.display_best_match(features)
        

