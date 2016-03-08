# csv playback

plays back data.csv over a socket.io connection.

## quickstart

    npm install
    npm start

## developing

    lein figwheel

in another terminal window,

    npm run dev

now, as you edit `/src/gyros_server/core.cljs`, the server will build incrementally while running.

## distributing

to make a prod build

   npm run build
