(ns figwheel-example.core
  (:require [clojure.pprint :refer [pprint]]
            [com.stuartsierra.component :as component]
            [compojure.core :refer [routes GET defroutes]]
            [ring.middleware.reload :refer [wrap-reload]]
            [org.httpkit.server :refer [run-server send!]]
            [figwheel-sidecar.components.figwheel-server :refer [figwheel-server]]
            [figwheel-sidecar.components.cljs-autobuild :refer [cljs-autobuild]]
            [figwheel-sidecar.channel-server :refer [eval-cljs]]
            [figwheel-example.state]))

(defn watch-state
  ([figwheel-server symbol]
   ;; TODO: require symbol's ns
   (let [atom @(resolve symbol)
         watch-key (str "figwheel-state-" symbol)]
     (remove-watch atom watch-key)
     (add-watch atom watch-key (fn [_ _ _ new-state] (watch-state figwheel-server symbol new-state)))
     (watch-state figwheel-server symbol @atom)))
  ([figwheel-server symbol state]
   (let [code (str "(cljs.core/reset! " symbol " " (pr-str state) ")")]
     (eval-cljs figwheel-server "dev" code))))

(defroutes app-routes
  (GET "/test" req (str "<html><body><pre>req: " (System/currentTimeMillis) ":" (-> req pprint with-out-str) "</pre></body></html>")))

(defrecord App [server figwheel port]
  component/Lifecycle
  (start [this]
    (let [all-routes (routes #'app-routes (:handler figwheel))]
      ;; TODO use http-server component
      (assoc this :server (run-server (wrap-reload all-routes) {:port port}))))
  (stop [{:keys [server] :as this}]
    (if (fn? server) (server))
    (assoc this :server nil)))

(defn new-app [opts]
  (map->App opts))

(defn dev-system []
  (-> (component/system-map
       :figwheel (figwheel-server
                  {:figwheel-options
                   {:httpless true
                    ;; :on-connect (fn [_ ch] (send! ch (pr-str {:build-id "dev" :msg-name :repl-eval :code "console.log('HOOK')"})))
                    :on-connect (fn [s _] (watch-state s 'figwheel-example.state/state))}})
       :cljsbuild (cljs-autobuild {:build-id "dev"})
       :app (new-app {:port 3449}))
      (component/system-using
       {:figwheel []
        :cljsbuild [:figwheel]
        :app [:figwheel]})))
