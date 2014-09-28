FordFiler-Zappos
================

Code for Zappo's 2014 Summer Intership Application

Problem Statement:
When giving gifts, consumers usually keep in mind two variables - cost and quantity. In order to facilitate better gift-giving on the Zappos website, the Software Engineering team would like to test a simple application that allows a user to submit two inputs: N (desired # of products) and X (desired dollar amount). The application should take both inputs and leverage the Zappos API (http://developer.zappos.com/docs/api-documentation) to create a list of Zappos products whose combined values match as closely as possible to X dollars. For example, if a user entered 3 (# of products) and $150, the application would print combinations of 3 product items whose total value is closest to $150.

Program operation:
The program takes the two inputs and uses them to do the following.

1. Download all relevant data from Zappos, that is all products whose price falls between 0 and (totalPrice - ((numGifts-1)*lowestPrice). That is to say all products that may actually be useful in making gift combinations.
During the process of downloading the products, also store a list of price points rounded to the nearest whole dollar. Since most products are priced at *.99, *.95, and *.00, rounding gives only a very small variance from the total requested price. In the worst case, the total's end up being off by a few dollars. This trade off seems worth it as it exponentially simplifies combination finding.

2. Use the price points stored to generate all possible combinations of price points that add up to the desired total price.

3. Display to the user a list of all price point combinations with the ability to view all product choices at each price point.


Compilation details
The Zappos folder in this repository is an Eclipse project, which should be importable into Eclipse for easy excution.
If compiling in a different way, be sure to include the packaged json.jar in the buildpath, as it is necessary for processing API responses
