class Ping extends HTMLElement {
    
    connectedCallback() {
        const source = new EventSource("http://localhost:2765/sse/resources/ping/subscribe");
        source.addEventListener('message', function(e) {
            console.log(e.data);
        }, false);
    }
}

customElements.define("a-post", Ping);
