(ns tarq-cljs.env
  (:require [environ.core :refer [env]]))

(defmacro cljs-env [key]
  (env key))

