(ns learning-three-cljs.core)

(enable-console-print!)

(defn init []
  (letfn [(make-renderer []
            (doto (js/THREE.WebGLRenderer.)
              (.setClearColor (js/THREE.Color. 0xEEEEEE))
              (.setSize (.-innerWidth js/window) (.-innerHeight js/window))
              (-> .-shadowMap .-enabled (set! true))))
          (make-plane []
                      (doto (js/THREE.Mesh.
                             (js/THREE.PlaneGeometry. 60 20 1 1)
                             (js/THREE.MeshLambertMaterial. (js-obj "color" 0xffffff)))
                        (-> .-rotation (.set (* -0.5 js/Math.PI) 0 0))
                        (-> .-position (.set 15 0 0))
                        (-> .-receiveShadow (set! true))))
          (make-cube []
                     (doto (js/THREE.Mesh.
                            (js/THREE.BoxGeometry. 4 4 4)
                            (js/THREE.MeshLambertMaterial. (js-obj "color" 0xff0000)))
                        (-> .-position (.set -4 3 0))
                        (-> .-castShadow (set! true))))
          (make-sphere []
                       (doto (js/THREE.Mesh.
                              (js/THREE.SphereGeometry. 4 20 20)
                              (js/THREE.MeshLambertMaterial. (js-obj "color" 0x7777ff)))
                         (-> .-position (.set 20 4 2))
                         (-> .-castShadow (set! true))))
          (make-camera []
                       (doto (js/THREE.PerspectiveCamera. 45 (/ (.-innerWidth js/window)
                                                                (.-innerHeight js/window))
                                                          0.1
                                                          1000)
                         (-> .-position (.set -30 40 30))))
          (make-spot-light []
                           (doto (js/THREE.SpotLight. 0xffffff)
                             (-> .-position (.set -40 60 -10))
                             (-> .-castShadow (set! true))))]
    (let [scene (js/THREE.Scene.)
          camera (make-camera)
          renderer (make-renderer)
          axes (js/THREE.AxisHelper. 20)
          plane (make-plane)
          cube (make-cube)
          sphere (make-sphere)
          spot-light (make-spot-light)]
      (.lookAt camera (.-position scene))
      (.add scene
            axes
            plane cube sphere
            camera
            spot-light)
      (-> js/document
          (.getElementById "app")
          (.appendChild (.-domElement renderer)))
      (.render renderer scene camera))))

(set! (.-onload js/window) init)

; (set! (.-innerHTML (js/document.getElementById "app"))
;       "<h1>Hello Chestnut!</h1>")
