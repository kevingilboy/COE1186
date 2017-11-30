clear;
m=40900;
v=30.2928;

kp=20000;
ki=25*kp^2/123897552;

sys = tf([v*m 0 0],[v*m kp ki]);
roots(sys.den{:})
rlocus(sys)
