#!/bin/bash

# Lista portów do zwolnienia
PORTS=("18080" "9090")

echo ">>> Zwalanianie portów: ${PORTS[*]}"

for PORT in "${PORTS[@]}"; do
  echo ">>> Sprawdzam port $PORT..."

  # Znajdź PID procesu
  PID=$(lsof -ti tcp:$PORT)

  if [ -n "$PID" ]; then
    echo ">>> Zabijam proces PID=$PID (port $PORT)"
    kill -9 $PID
  else
    echo ">>> Brak lokalnego procesu na porcie $PORT"
  fi

  # Znajdź kontener Dockera
  CID=$(docker ps --filter "publish=$PORT" --format "{{.ID}}")

  if [ -n "$CID" ]; then
    echo ">>> Zatrzymuję kontener Dockera na porcie $PORT (CID=$CID)"
    docker stop $CID
  else
    echo ">>> Brak kontenera Dockera na porcie $PORT"
  fi

done

echo ">>> Wszystko czyste!"
