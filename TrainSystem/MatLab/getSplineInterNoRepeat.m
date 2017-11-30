function [ x_ticks,y_ticks ] = getSplineInterNoRepeat( imgname, track_length )
%UNTITLED6 Summary of this function goes here
%   Detailed explanation goes here

I=imread(imgname);
imshow(I);
I=rgb2gray(I);

[row,col] = find(I<200);
points = [col,row];

[end1,end2] = findEndpoints(I);

if length(unique(points(:,1)))==1 && length(points(:,1))>1   
    y =linspace(end1(2),end2(2),track_length);
    x = ones(length(y),1) * points(1,1);
else
    [~,unique_rows] = unique(points(:,1));
    unique_points = points(unique_rows,:);
    unique_points(find(unique_points(:,1)==end1(1)),:) = end1;
    unique_points(find(unique_points(:,1)==end2(1)),:) = end2;
    x = unique_points(:,1);
    y = unique_points(:,2);
    
    minx = min(unique_points(:,1));
    maxx = max(unique_points(:,1));
    x = linspace(minx,maxx,1000)';
    f=fit(unique_points(:,1),unique_points(:,2),'smoothingspline','SmoothingParam',.7);
    y=feval(f,x);
end

lol = interparc(round(track_length),x,y,'spline');
x_ticks = lol(:,1);
y_ticks = lol(:,2);

end

