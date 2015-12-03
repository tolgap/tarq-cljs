(ns tarq-cljs.dev
  (:require [tarq-cljs.core :as core]
            [figwheel.client :as figwheel :include-macros true]))

(enable-console-print!)

(defn on-js-reload []
  (core/app))

(core/app)
