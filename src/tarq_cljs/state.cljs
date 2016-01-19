(ns tarq-cljs.state)

(defonce app-state
  (atom {:page :websites
         :params {}
         :websites []
         :search-set []
         :filters {}
         :vulnerabilities {:objects []
                           :search-set []
                           :filters {}}}))
