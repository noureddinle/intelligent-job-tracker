import express from "express";
import { WebSocketServer } from "ws";
import { v4 as uuidv4 } from "uuid";
import cors from "cors";

const app = express();
app.use(cors());
const PORT = process.env.PORT || 9000;

app.get("/", (req, res) => {
  res.send({ message: "✅ WebSocket signaling server running" });
});

const wss = new WebSocketServer({ noServer: true });
const rooms = new Map();

wss.on("connection", (ws, req) => {
  let currentRoom = null;

  ws.on("message", (msg) => {
    const data = JSON.parse(msg.toString());
    console.log("📨 Message:", data);

    switch (data.type) {
      case "join":
        currentRoom = data.roomId;
        if (!rooms.has(currentRoom)) rooms.set(currentRoom, []);
        rooms.get(currentRoom).push(ws);
        console.log(`👥 User joined room ${currentRoom}`);
        break;

      case "signal":
        if (currentRoom && rooms.has(currentRoom)) {
          rooms.get(currentRoom).forEach(client => {
            if (client !== ws && client.readyState === client.OPEN) {
              client.send(JSON.stringify({ type: "signal", data: data.data }));
            }
          });
        }
        break;

      case "leave":
        if (currentRoom && rooms.has(currentRoom)) {
          rooms.set(
            currentRoom,
            rooms.get(currentRoom).filter(client => client !== ws)
          );
          console.log(`🚪 User left room ${currentRoom}`);
        }
        break;
    }
  });

  ws.on("close", () => {
    if (currentRoom && rooms.has(currentRoom)) {
      rooms.set(
        currentRoom,
        rooms.get(currentRoom).filter(client => client !== ws)
      );
    }
  });
});

const server = app.listen(PORT, () => {
  console.log(`🚀 WebSocket signaling server on port ${PORT}`);
});
server.on("upgrade", (req, socket, head) => {
  wss.handleUpgrade(req, socket, head, (ws) => {
    wss.emit("connection", ws, req);
  });
});
