> Our database based on Google datastore API
--------------------

#### User: <b>Kind</b>

|Key|ID|email|
|----|----|----|
|Auto-generated ID|Google ID|preserved for future use|

--------------------
#### Post: <b>Kind</b>
> Posts that are not replied

|Key|content|timestamp|user_id|
|----|----|----|----|
|Auto-generated ID|Content|for display ordering|Owner's auto-generated ID|

------------------

#### RepliedPost: <b>Kind</b>
> Posts that are replied

|Key|content|originalcontent|ownerId|timestamp|
|----|----|----|----|----|
|Auto-generated ID|Content|Reply|Owner's auto-generated ID|for display ordering|


