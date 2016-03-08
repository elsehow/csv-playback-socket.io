(ns gyros-server.core
  (:require [cljs.nodejs :as nodejs]))

(def port 3333)

(nodejs/enable-util-print!)

(def fs
  (nodejs/require "fs"))

(def io
  (let [app (.createServer (nodejs/require "http"))]
    ((nodejs/require "socket.io") app)
    (.listen app port)))

(def csv-file
   (.readFileSync fs "data.csv"))

(def csv-lines
  (rest (.split (.toString csv-file) "\n")))

(defn to-map [csv-line]
  (let [line (.split csv-line ",")]
    (zipmap [:timestamp :imu :yaw :pitch :roll :rssi] line)))

(def data-points
  (map to-map csv-lines))

(def time-deltas
  (let [initial (:timestamp (first data-points))]
    (map
      #(- (:timestamp %) initial)
      data-points)))

(defn set-timeouts [cb]
  (doall
    (map
      (fn [data time] (js/setTimeout #(cb data) time))
      data-points
      time-deltas)))

(defn -main []
  (.on io "connection"
    (fn [socket]
      (set-timeouts socket.emit)))
  (println (str "listening on " port)))

(set! *main-cli-fn* -main)