function detect = inference(filename)
a = imread(filename);
load 'net.mat';

output = classify(net,a);

disp(output);
%disp(class(char(output)));
detect = char(output);
end