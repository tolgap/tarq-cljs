(ns tarq-cljs.state)

(defonce app-state
  (atom {:page nil
         :params {}
         :websites []
         :search-set []
         :filters {}
         :vulnerabilities {:objects []
                           :search-set []
                           :filters {}}}))
