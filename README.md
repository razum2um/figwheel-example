# Figwheel example

![screen](https://raw.github.com/razum2um/figwheel-example/master/sysmap.png)

# Setup

    # clone
    user=> (go)
    # open http://localhost:3449
    user=> (swap! figwheel-example.state/state (fn [a] (update-in a [:a] inc)))
