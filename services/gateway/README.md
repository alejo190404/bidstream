This folder contains the Java Spring Boot API gateway, responsible for routing requests, authentication, and load balancing for the BidStream services.

# Test the MongoDB Container
Once your services are up and running, you can verify the state of your MongoDB instance using the mongosh (MongoDB Shell) directly from the container.

1. Access the Container in Interactive Mode
To enter the container and start the MongoDB shell, run:

Bash
docker exec -it bidstream-mongodb mongosh
2. Database and Collection Navigation
Once inside the mongosh shell, use these commands to explore your data:

List all databases:

JavaScript
show dbs
Switch to your database:
(Based on your config, the database is subastas)

JavaScript
use subastas
List all collections in the current database:

JavaScript
show collections
3. Inspect and Query Documents
Replace <collection_name> with the name of the collection you want to inspect.

View all documents (limit 10):

JavaScript
db.<collection_name>.find().limit(10)
Count total documents:

JavaScript
db.<collection_name>.countDocuments()
4. Filter by Specific Fields
Here are a few examples of how to run filtered queries:

Exact match:

JavaScript
db.<collection_name>.find({ status: "active" })
Filter by ID:

JavaScript
db.<collection_name>.find({ _id: ObjectId("your_id_here") })
Numerical comparison (e.g., price greater than 100):

JavaScript
db.<collection_name>.find({ price: { $gt: 100 } })
Logical AND (multiple fields):

JavaScript
db.<collection_name>.find({ category: "electronics", stock: { $lt: 5 } })
5. Exit the Shell
To leave the MongoDB shell and return to your terminal:

JavaScript
exit