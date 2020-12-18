#!python3
# -*- coding: UTF-8 -*-

from functools import reduce
import curses

print('✓')

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
                r = t_sum(t, clazz['items'].values())
                clazz['b'].append(r / len(clazz['items']))

        b_avg = []
        for t in range(self.attrs_len):
            bs = map(lambda c: c['b'], self.clazzes.values())
            b_t = t_sum(t, bs) / len(self.clazzes)
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