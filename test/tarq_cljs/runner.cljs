(ns tarq-cljs.runner
  (:require  [doo.runner :refer-macros [doo-tests]]
             [tarq-cljs.core-test]
             [tarq-cljs.website-test]
             [tarq-cljs.plugin-test]
             [tarq-cljs.vulnerability-test]))

(doo-tests 'tarq-cljs.core-test
           'tarq-cljs.website-test
           'tarq-cljs.plugin-test
           'tarq-cljs.vulnerability-test)
