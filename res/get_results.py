"""
Usage: get_results.py PATH_BID_CSV PATH_ASK_CSV
"""

import pandas as pd
import numpy as np
import sys
from math import log
from functools import reduce

def tail_exp(series, k):
    sorted_series = sorted(series, reverse=True)
    tail = sorted_series[:k]
    log_tail = list(map(lambda x : log(x) if x != 0 else log(0.0004), tail))
    return 1/(sum(log_tail[:-1]) / len(log_tail[:-1]) - log_tail[-1])
    
if __name__ == "__main__":
    if (len(sys.argv) < 3):
        print(__doc__)
        exit()
    bid_data = pd.read_csv(sys.argv[1])
    ask_data = pd.read_csv(sys.argv[2])
    assert reduce(
        lambda x, y : x and y, 
        [str(b) == str(a) for b, a in zip(bid_data['Local time'], ask_data['Local time'])], 
        True)
    bid = bid_data['Close'].values
    ask = ask_data['Close'].values
    logv = np.vectorize(log)
    absv = np.vectorize(abs)
    log_bid = logv(bid)
    log_ask = logv(ask)
    mid_price = 0.5 * (log_ask + log_bid)
    log_ret = absv(mid_price[1:] - mid_price[:-1])
    spread = log_ask - log_bid
    print("Avg log ret: {}".format(np.mean(log_ret)))
    print("Std log ret: {}".format(np.std(log_ret)))
    print("Tail log ret: {}".format(tail_exp(log_ret[:30000], 1000)))
    print("Avg spread: {}".format(np.mean(spread)))
    print("Std spread: {}".format(np.std(spread)))