(ns learning-three-cljs.core)

(enable-console-print!)

(set! (.-innerHTML (js/document.getElementById "app"))
      "<h1>Hello Chestnut!</h1>")
