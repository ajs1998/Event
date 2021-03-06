# Simple event publishing system for Java

## What is it?

It's a tool for using the Publish-Subscribe pattern.
You can implement as many of your own `EventListener` types as you need, and your listeners may `@Subscribe` to as many
events as you need.
When an event is published, methods that subscribe to it, or a super class, will be invoked.
You may supply your own `Executor` to publish events with 

## What can I do with it?

### Publish events

```java
// Create a new EventPublisher
EventPublisher publisher = new EventPublisher();

// Register some listeners that implement EventListener
publisher.register(new MyListenerForLogging());
publisher.register(new MyListenerThatCounts());

// Publish an event for our listeners to handle
publisher.publish(new MyEvent());
```

### Create listeners

```java
class MyListenerForLogging implements EventListener {

    @Subscribe
    public void listenForMyEvent(MyEvent event) {
        System.out.println("This action just happened: " + event.getAction());
    }

    @Subscribe
    public void didActionFail(MyEvent event) {
        if (!event.getStatus()) {
            System.out.err("Uh oh, failed to " + event.getAction());
        }
    }

}

class MyListenerThatCounts implements EventListener {

    private final AtomicInteger counter = new AtomicInteger(0);

    @Subscribe
    public void listenForMyEvent(MyEvent event) {
        counter.getAndIncrement();
    }

    public void getCount() {
        return counter.get();
    }

}
```

### Create events

```java
// Very basic data object
class MyEvent {

    private String action;
    private boolean status;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
```

### Publish with your own `Executor`
```java
// Create a new EventPublisher that uses a fixed-size thread pool
EventPublisher publisher = new EventPublisher(Executors.newFixedThreadPool(4));
```

## How do I get it?

### Gradle package from GitHub Packages

```gradle
repositories {
    mavenCentral()
    maven {
        url = uri('https://maven.pkg.github.com/ajs1998/Event')
        credentials {
            username = {YOUR GITHUB USERNAME}
            // This is a PAT (Personal Access Token) that only has permission to read/download public GitHub Packages.
            // This is not the actual password for the account.
            password = {YOUR GITHUB PAT}
        }
    }
}
```

```gradle
dependencies {
    implementation 'me.alexjs:event:2.0.0'
}
```

You need to <a href="https://github.com/settings/tokens">create a GitHub Personal Access Token (PAT)</a> to be able to
download GitHub Packages.
The token only needs the `read:packages` permission to work.

### Maven Central package

Coming soon

## Notes

- Dead-simple `EventPublisher`
- 100% test coverage
- 100% Javadoc coverage
