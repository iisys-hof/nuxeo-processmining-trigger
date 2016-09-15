# nuxeo-processmining-trigger
Triggers the "document modified" event. If the version of the document is "1.0" (for the first time), then a http-REST call to Shindig is triggered:
> {shindig-url}/social/rest/processmining/{docType}

The sent json-body contains: docId, type, startDate, endDate, userList 

### Config:

Put the following lines in {nuxeo}/bin/nuxeo.config:
```
##-----------------------------------------------------------------------------
## Plugin FinalVersionDocListener
##-----------------------------------------------------------------------------
shindig_api_url=http://127.0.0.1:8080/social/rest
```

Use the real shindig-url! Restart Nuxeo.

### Install in Nuxeo:

1. Import Project into Nuxeo IDE
2. Right Click on Project -> Nuxeo -> export jar
3. Place jar in directory *nxserver/plugins/*
4. Restart Nuxeo