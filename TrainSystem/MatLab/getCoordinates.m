function [ output ] = getCoordinates( line_color, letters_to_flip )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here

line = readtable(strcat(line_color,'LineFinal.csv'));

letters = unique(line{:,2});
[rows,cols] = size(line);

output=string(zeros(rows,1));

curr_row=1;
for i=1:length(letters)
    letter = letters(i);
    disp(letter);
    indices = find(strcmp(line{:,2},letter));
    lengths = line{indices,4};
    [x,y] = getSplineInterNoRepeat(char(strcat(line_color,'/',line_color,'_',letter,'.png')),sum(lengths)+length(lengths));

    startindex = 1;
    endindex = sum(lengths)+length(lengths);
    
    for j=1:length(indices)
        if any(strcmp(letter,letters_to_flip))
            xvals = x(round(endindex)-round(lengths(j)):round(endindex));
            yvals = y(round(endindex)-round(lengths(j)):round(endindex));
            endindex = endindex-round(lengths(j)-1);
            
            xvals = flipud(xvals);
            yvals = flipud(yvals);
        else
            xvals = x(startindex:startindex+round(lengths(j)));
            yvals = y(startindex:startindex+round(lengths(j)));
            startindex = startindex+round(lengths(j))+1;            
        end

        running_row = '';
        for k=1:length(xvals)-1
            midx = (xvals(k)+xvals(k+1))/2;
            midy = (yvals(k)+yvals(k+1))/2;
            running_row = strcat(running_row,sprintf('%f_%f;',midx,midy));
        end
        output(curr_row,1) = running_row;
        
        curr_row=curr_row+1;
    end
end

end

