(ns clojure-getting-started.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (pr-str ["Hello" :from 'Heroku])})

(defn date []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (clj-time.format/unparse (clj-time.format/formatter "yyyy-MM-dd") (clj-time.local/loca))})

(defroutes app
  (GET "/" []
       (splash))
  (GET "/date" []
        (date))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))
