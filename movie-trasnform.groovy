import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.json.JsonBuilder


def jsonSlurper = new JsonSlurper()
def movie = jsonSlurper.parseText(new String(payload))
//def movie = new JsonBuilder()


def connection = new URL( "https://movies-tv-shows-database.p.rapidapi.com/?movieid=tt0133093")
                 .openConnection() as HttpURLConnection

connection.setRequestProperty( 'Type', 'get-movie-details')
connection.setRequestProperty( 'x-rapidapi-host', 'movies-tv-shows-database.p.rapidapi.com' )
connection.setRequestProperty( 'x-rapidapi-key', '7279d15fb8mshb3f908bf55c57cdp17f62fjsn495f2bb219071928')  //<-- Change Me
connection.setRequestProperty( 'Accept', 'application/json' )
connection.setRequestProperty( 'Content-Type', 'application/json')

if ( connection.responseCode == 200 ) {
    
    def imdb = connection.inputStream.withCloseable { inStream ->
        new JsonSlurper().parse( inStream as InputStream )
    }

    movie.imdb = [ "rating": imdb.imdb_rating, "ratingCount": imdb.vote_count ]

} else {
    println connection.responseCode + ": " + connection.inputStream.text
}

JsonOutput.toJson(movie)
