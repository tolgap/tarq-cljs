(ns tarq-cljs.runner
  (:require  [doo.runner :refer-macros [doo-tests]]
             [tarq-cljs.core-test]
             [tarq-cljs.website-test]))

(doo-tests 'tarq-cljs.core-test
           'tarq-cljs.website-test)
