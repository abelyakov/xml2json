# xml2json
Simple tool to convert xml document to json representation and back. Java 8 required. Can be used as inner util. Result is pretty-printed

## Usage

xml to json:
```
java -jar xml2json-0.0.1.jar test.xml 

```
json to xml:
```
java -jar xml2json-0.0.1.jar -r test.json
```
## Examples

Simple example:
```
<one>
    <two>2</two>
    <three>3</three>
</one>

//output:

{
    "one":{
        "three":"3",
        "two":"2"
    }
}
```
Xml-attributes will be converted to json-field with name "attr":

```
<one level="1"></one>


//output:

{
    "one":{
        "attrs":{
            "level":"1"
        }
    }
}
```
If xml tag has both text value and attributes, then text value will be converted to json-propery with name '@':
```
<one level="1">title</one>

output:

{
    "one":{
        "attrs":{
            "level":"1"
        },
        "@":"title"
    }
}
```
Multiple inner nodes with same tag will be converted to json-array:
```
<numbers type="array">
    <value>1</value>
    <value>2</value>
    <value>3</value>
    <value>4</value>
</numbers>

//output:

{
    "numbers":{
        "value":[
            "4",
            "3",
            "2",
            "1"
        ],
        "attrs":{
            "type":"array"
        }
    }
}

```
