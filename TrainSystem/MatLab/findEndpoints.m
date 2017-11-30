function [ point1, point2 ] = findEndpoints( I )
%UNTITLED4 Summary of this function goes here
%   Detailed explanation goes here

[rows,cols] = size(I);
point1=0;
point2=0;
threshold = 200;

for x=2:rows-1
    for y=2:cols-1
        if( I(x,y) < threshold)
            neighbors = 0;
            if(I(x+1,y) < threshold)
                neighbors = neighbors + 1;
            end
            if(I(x-1,y) < threshold)
                neighbors = neighbors + 1;
            end
            if(I(x+1,y-1) < threshold)
                neighbors = neighbors + 1;
            end
            if(I(x-1,y-1) < threshold)
                neighbors = neighbors + 1;
            end
            if(I(x+1,y+1) < threshold)
                neighbors = neighbors + 1;
            end
            if(I(x-1,y+1) < threshold)
                neighbors = neighbors + 1;
            end
            if(I(x,y+1) < threshold)
                neighbors = neighbors + 1;
            end
            if(I(x,y-1) < threshold)
                neighbors = neighbors + 1;
            end
            if(neighbors==1)
                if(point1==0)
                    point1=[y,x];
                else
                    point2=[y,x];
                end
            end
        end
    end
end

