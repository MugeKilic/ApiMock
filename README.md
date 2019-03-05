### Trendyol Api Mock Testing ###

Before starting this tests, you should build mountebank directory with docker..

First of all you should change directory as `mountebank`, then;

`docker build -t trendyol_stubs .`
`docker run -p 2525:2525 -p 2005:2005 trendyol_stubs`

Then you would run any test belows to src/test.

Not forget killing trendyol_stubs after running tests.
