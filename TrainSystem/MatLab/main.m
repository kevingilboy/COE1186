clear

line_color = 'green';

if strcmp(line_color,'red')
    letters_to_flip = {'D','E','F','G','I','J','K','O','P','R','S'};
elseif strcmp(line_color,'green')
    letters_to_flip = {'C2','D','E','L','M','N','O','P1','V','U','W','X','Y'};  
else
    disp('Sad!')
    return
end

output = getCoordinates(line_color,letters_to_flip);

fid = fopen(strcat(line_color,'_coordinates.txt'),'wt');
for i=1:length(output)
    fprintf(fid,'%s', output(i));
    fprintf(fid,'\n');
end

verify(strcat(line_color,'_coordinates.txt'))
fclose(fid);