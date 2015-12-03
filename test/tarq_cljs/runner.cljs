(ns tarq-cljs.runner
  (:require  [doo.runner :refer-macros [doo-tests]]
             [tarq-cljs.core-test]))

(doo-tests 'tarq-cljs.core-test)
