echo '-------------------'
echo '|                 |'
echo '|    Compiling    |'
echo '|                 |'
echo '-------------------'

javac Shared/Module.java
javac Shared/SimTime.java
javac Modules/Ctc/Ctc.java
javac Modules/TrackController/TrackController.java
javac Modules/TrackController/TrackControllerGUI.java
javac Modules/TrackController/PLC.java
javac Modules/TrackModel/TrackModel.java
javac Modules/TrainModel/TrainModel.java
javac Modules/TrainController/TrainController.java
javac Modules/Mbo/Mbo.java
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
