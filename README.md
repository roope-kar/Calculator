# Calculator service
service that calculates the provided equasion. Supported operations: * + - / ( )

https://intense-coast-33652.herokuapp.com/

# Usage
1. encode an equasion (Base64 + UTF-8)  
2. copy the encoded equasion and pass it as query param: https://intense-coast-33652.herokuapp.com/calculus?query=
3. if query was valid equasion, json blob is returned: {"result":123}
4. if query was invalid equasion, json blob is also returned: {"error":"invalid equasion", "message":"..."}
