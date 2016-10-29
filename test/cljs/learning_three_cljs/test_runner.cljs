(ns learning-three-cljs.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [learning-three-cljs.core-test]
   [learning-three-cljs.common-test]))

(enable-console-print!)

(doo-tests 'learning-three-cljs.core-test
           'learning-three-cljs.common-test)
