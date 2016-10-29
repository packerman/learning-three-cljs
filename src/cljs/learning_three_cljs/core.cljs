(ns learning-three-cljs.core)

(enable-console-print!)

(defn init []
      (letfn [(make-renderer []
                    (doto (js/THREE.WebGLRenderer.)
                          (.setClearColor 0xEEEEEE)
                          (.setSize (.-innerWidth js/window) (.-innerHeight js/window))))
              (make-plane []
                (doto (js/THREE.Mesh.
                        (js/THREE.PlaneGeometry. 60 20 1 1)
                        (js/THREE.MeshBasicMaterial. (js-obj "color" 0xcccccc)))
                  (-> .-rotation (.set (* -0.5 js/Math.PI) 0 0))
                  (-> .-position (.set 15 0 0))))
              (make-cube []
                (doto (js/THREE.Mesh.
                        (js/THREE.BoxGeometry. 4 4 4)
                        (js/THREE.MeshBasicMaterial. (js-obj "color" 0xff0000
                                                             "wireframe" true)))
                    (-> .-position (.set -4 3 0))))
              (make-sphere []
                (doto (js/THREE.Mesh.
                        (js/THREE.SphereGeometry. 4 20 20)
                        (js/THREE.MeshBasicMaterial. (js-obj "color" 0x7777ff
                                                             "wireframe" true)))
                    (-> .-position (.set 20 4 2))))
              (make-camera []
                  (doto (js/THREE.PerspectiveCamera. 45 (/ (.-innerWidth js/window)
                                                           (.-innerHeight js/window))
                                                  0.1
                                                  1000)
                    (-> .-position (.set -30 40 30))))]
            (let [scene (js/THREE.Scene.)
                  camera (make-camera)
                  renderer (make-renderer)
                  axes (js/THREE.AxisHelper. 20)
                  plane (make-plane)
                  cube (make-cube)
                  sphere (make-sphere)]
              (.lookAt camera (.-position scene))
              (.add scene
                    axes
                    plane
                    cube
                    sphere
                    camera)
              (-> js/document
                  (.getElementById "app")
                  (.appendChild (.-domElement renderer)))
              (.render renderer scene camera))))

(set! (.-onload js/window) init)

; (set! (.-innerHTML (js/document.getElementById "app"))
;       "<h1>Hello Chestnut!</h1>")
