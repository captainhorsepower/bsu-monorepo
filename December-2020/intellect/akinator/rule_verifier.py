import importlib
from akinator import *


def main():
    filename = chooseDatabaseFile()
    rules, _ = loadRules(filename)
    verifyRules(rules)


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
            


if __name__ == "__main__":
    main()
