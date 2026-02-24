# Subasta queue (Kafka) – topics

Topics used by Gateway (producer) and Subastas (consumer):

| Topic | Purpose |
|-------|---------|
| `auction.create` | Create new auction |
| `auction.start-streaming` | Start Jitsi stream for an auction |
| `auction.end` | End auction and create payment for winner |
| `bid.create` | Place a bid (puja) |
| `stream.add-user` | Add user to streaming room (optional) |

Created at startup by `kafka-init` in `docker-compose.yml`.
