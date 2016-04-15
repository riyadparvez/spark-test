#!/usr/bin/env bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
SPARK_HOME="/home/riyad/dev-src/spark-1.6.1"

TWITTER_ACCESS_TOKEN="86737412-juMHvAhYHsKTDhs55bbaNtGvKeLybJ5957s7QfNwi"
TWITTER_ACCESS_SECRET="CvwHoGEyCqTNOuLj78KWaHt4NTp1HBqzpUTdWU8vqIZBJ"
TWITTER_CONSUMER_KEY="hCmDyloImK3XOsoKtqH1NVZcR"
TWITTER_CONSUMER_SECRET="wrue7RoUxFf2x1GOI9Rt1WHgDOxShpSciAPCq7rdB5PqQPZ9OV"

MAIN_CLASS=$1
JAR_FILE=$2

#${SPARK_HOME}/bin/spark-submit \
#  --class ${MAIN_CLASS} \
#  --master local[8] \
#  ${DIR}/target/scala-2.10/${JAR_FILE} \
#  --consumerKey ${TWITTER_CONSUMER_KEY} \
#  --consumerSecret ${TWITTER_CONSUMER_SECRET} \
#  --accessToken ${TWITTER_ACCESS_TOKEN}  \
#  --accessTokenSecret ${TWITTER_ACCESS_SECRET} \
#  /tmp/bla 10000 1 20

: '
${SPARK_HOME}/bin/spark-submit \
  --class ${MAIN_CLASS} \
  --master local[8] \
  ${DIR}/target/scala-2.10/${JAR_FILE} \
  "${YOUR_TWEET_INPUT:-/tmp/bla/tweets*/part-*}" \
  ${OUTPUT_MODEL_DIR:-/tmp/models} \
  ${NUM_CLUSTERS:-10} \
  ${NUM_ITERATIONS:-20}
'
  
#: '
${SPARK_HOME}/bin/spark-submit \
  --class ${MAIN_CLASS} \
  --master local[8] \
  ${DIR}/target/scala-2.10/${JAR_FILE} \
  --consumerKey ${TWITTER_CONSUMER_KEY} \
  --consumerSecret ${TWITTER_CONSUMER_SECRET} \
  --accessToken ${TWITTER_ACCESS_TOKEN}  \
  --accessTokenSecret ${TWITTER_ACCESS_SECRET} \
  ${OUTPUT_MODEL_DIR:-/tmp/models} \
  ${NUM_CLUSTERS:-10} \
#'
