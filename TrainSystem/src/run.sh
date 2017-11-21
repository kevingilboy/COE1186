echo '-------------------'
echo '|                 |'
echo '|    Compiling    |'
echo '|                 |'
echo '-------------------'
javac Simulator/Simulator.java

echo '> Done'
sleep 1

echo
echo '-------------------'
echo '|                 |'
echo '|     Running     |'
echo '|                 |'
echo '-------------------'
sleep 1

echo '*        . . . . o o o o o                        '
echo '*               _____      o       ___________    '
echo '*      ____====  ]OO|_n_n__][.     |   HSS   |    '
echo '*     [________]_|__|________)<    |COE 1186 |    '
echo '*      oo    oo   oo OOOO-| oo\\_  ~~~|~~~|~~~    '
echo '*  +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+-'

java Simulator.Simulator
