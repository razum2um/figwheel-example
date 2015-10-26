(ns figwheel-example.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [figwheel-example.state :refer [state]]))

(enable-console-print!)

(defn read [{:keys [state] :as env} key params]
  (let [st @state]
    (if-let [[_ value] (find st key)]
      {:value value}
      {:value :not-found})))

(defn mutate [{:keys [state] :as env} key params]
  (if (= 'increment key)
    {:value [:a]
     :action #(swap! state update-in [:a] inc)}
    {:value :not-found}))

(defui Root
  static om/IQuery
  (query [this]
         [:a])
  Object
  (render [this]
          (dom/div nil "Hello, figwheel, my state: " (pr-str (om/props this)))))

(def reconciler (om/reconciler {:state state
                                :parser (om/parser {:read read :mutate mutate})}))
(om/add-root! reconciler Root (gdom/getElement "app"))
