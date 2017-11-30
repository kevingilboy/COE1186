function [  ] = verify( fname )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here

fid = fopen(fname,'r');
data = textscan(fid,'%s');
fclose(fid);

line = string(data{1,1});
line = strjoin(line,';');
pairs = strsplit(line,';');
xcoords = zeros(1,length(pairs)-1);
ycoords = zeros(1,length(pairs)-1);
for i=1:length(pairs)-1
    disp(i)
    if i==150
       disp('')
    end
    pair = pairs(i);
    xcoords(i) = regexp(pair,'([0-9.]*(?=_))','match');
    ycoords(i) = regexp(pair,'((?<=_)[0-9.]*)','match');
end
figure
scatter(xcoords,-1.*ycoords,'.');
hold on
for i=1:length(xcoords)
    %scatter(xcoords(i),-1*ycoords(i),'.')
    %pause(1/1000000)
end
hold off
end

