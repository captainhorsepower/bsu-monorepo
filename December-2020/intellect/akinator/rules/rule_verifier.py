import importlib
from akinator import *


def main():
    filename = chooseDatabaseFile()
    rules, t = chooseRules(filename)
    hah([r for r in rules if r.canAnswer(t)])

def hah(rules):
    keys = sharedWhenKeys(rules)
    print(f'shared when {keys}')

    keysILike = ['основа', 'добавка к основе']
    for r in rules:
        context = {k: r.getWhen()[k] for k in keysILike}
        rulesLeft = [r.getThen() for r in rules if r.when(context)[0] != RuleState.FAIL]
        tmp = sum(map(lambda d: list(d.values()), rulesLeft), start=[])
        print(f'{tmp} possible when {context}')
    

def sharedWhenKeys(rules):
    keys = list(set(
        sum(map(lambda r: list(r.getWhen().keys()), rules), start=[])))
    return keys

def verifyRules(rules):
    """
    Rules can contradict if their 'then' keys match.

    Rule is invalid when:
    - it's redundant. Exists 'prefix rule'
      - same 'then'
      - 'when' is a subset current 'when'.
    - exists another rule with
      - 'when' don't contradict
      - 'then' do
    """

    rulesByThenClause = {}
    for r in rules:
        for k in r.getThen():
            rules = rulesByThenClause.get(k, [])
            rulesByThenClause[k] = rules + [r]

    for k, rules in rulesByThenClause.items():
        for r in rules:
            d = whenClausesMathch_thenClausesDont(k, r, rules)
            if d['DO'] or d['CAN']:
                print(r, d)


def whenClausesMathch_thenClausesDont(key, rule, others):
    context = rule.getWhen()
    d = {'DO': [], 'CAN': []}
    for other in [r for r in others if rule != r and rule.getThen()[key] != r.getThen()[key]]:
        status, _ = other.when(context)
        if status == RuleState.SUCCESS:
            d['DO'].append(other)
        elif status == RuleState.UNKNOWN:
            d['CAN'].append(other)

    return d


def ruleRepr(self) -> str:
        return 'rule repr'


def ruleToStr(self) -> str:
        return 'rule str'

# Rule.__repr__ = ruleRepr
# Rule.__str__ = ruleToStr          


if __name__ == "__main__":
    main()
