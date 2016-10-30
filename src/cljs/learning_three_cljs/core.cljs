(ns learning-three-cljs.core)

(enable-console-print!)

(defn init []
  (letfn [(make-renderer []
            (doto (js/THREE.WebGLRenderer.)
              (.setClearColor (js/THREE.Color. 0xeeeeee))
              (.setSize (.-innerWidth js/window) (.-innerHeight js/window))
              (-> .-shadowMap .-enabled (set! true))))
          (make-plane []
                      (doto (js/THREE.Mesh.
                             (js/THREE.PlaneGeometry. 60 20 1 1)
                             (js/THREE.MeshLambertMaterial. (js-obj "color" 0xaaaaaa)))
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
                             (-> .-castShadow (set! true))
                             (-> .-shadow .-mapSize .-width (set! 1024))
                             (-> .-shadow .-mapSize .-height (set! 1024))))
          (change-coords [object dx dy dz]
              (.set object
                    (+ (.-x object) dx)
                    (+ (.-y object) dy)
                    (+ (.-z object) dz)))
          (change-rotation [node dx dy dz]
              (change-coords (.-rotation node) dx dy dz))
          (change-position [node dx dy dz]
              (change-coords (.-position node) dx dy dz))
          (init-stats []
              (doto (js/Stats.)
                (.setMode 0)
                (->> .-domElement (.appendChild (.getElementById js/document "stats")))))]
    (let [scene (js/THREE.Scene.)
          camera (make-camera)
          renderer (make-renderer)
          axes (js/THREE.AxisHelper. 20)
          plane (make-plane)
          cube (make-cube)
          sphere (make-sphere)
          spot-light (make-spot-light)
          ambient-light (js/THREE.AmbientLight. 0x606060)
          stats (init-stats)
          step (atom 0)
          controls (js-obj
                        "rotationSpeed" 0.02
                        "bouncingSpeed" 0.03)
          gui (doto (js/dat.GUI.)
                (.add controls "rotationSpeed" 0 0.5)
                (.add controls "bouncingSpeed" 0 0.5))
          render-scene (fn render-scene []
                            (.update stats)
                            (swap! step + (.-bouncingSpeed controls))
                            (change-rotation cube (.-rotationSpeed controls)
                                                  (.-rotationSpeed controls)
                                                  (.-rotationSpeed controls))
                            (doto sphere
                                (-> .-position .-x (set! (+ 20 (* 10 (js/Math.cos @step)))))
                                (-> .-position .-y (set! (+ 2 (* 10 (js/Math.abs (js/Math.sin @step)))))))
                            (js/requestAnimationFrame render-scene)
                            (.render renderer scene camera))
          on-resize (fn []
                        (set! (.-aspect camera) (/ (.-innerWidth js/window)
                                                   (.-innerHeight js/window)))
                        (.updateProjectionMatrix camera)
                        (.setSize renderer (.-innerWidth js/window) (.-innerHeight js/window)))]
      (.addEventListener js/window "resize" on-resize false)
      (.lookAt camera (.-position scene))
      (.add scene
            axes
            plane cube sphere
            camera
            spot-light
            ambient-light)
      (-> js/document
          (.getElementById "app")
          (.appendChild (.-domElement renderer)))
      (render-scene))))

(set! (.-onload js/window) init)
