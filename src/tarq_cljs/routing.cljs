(ns tarq-cljs.routing
  (:require [tarq-cljs.state :refer [app-state]]
            [secretary.core :as secretary :refer-macros [defroute]]
            [accountant.core :as accountant]))

(accountant/configure-navigation!)

(defroute servers "/servers" []
  (swap! app-state assoc
         :page :servers))

(defroute website "/websites/:id" [id]
  (swap! app-state assoc
         :page :websites
         :params {:id id
                  :current-website {}}))

(defroute vulnerabilities "/vulnerabilities" []
  (swap! app-state assoc
         :page :vulnerabilities))

(defroute websites "/" []
  (swap! app-state assoc
         :page :websites))

(defroute "*" []
  (swap! app-state assoc
         :page :404))

(accountant/dispatch-current!)
