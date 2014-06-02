#
# Clients
#   Signatures
#

Client3
Dependencies
    Apache HttpClient
    Json.org
Prerequisites
    You must have created a TextIndex 'geneontologyindex' or rename the index id
Signatures  
    public String addContentToIndex(String myIndex, String urlToIndex)
    public void getJobStatus(String reference)
    public void listIndex()
	