%a = imread('C:\Users\Surya Reddy\Desktop\ImageProcessing-Matlab\Train\healthy\1.jpg');
%ans = infer('C:\Users\Surya Reddy\Desktop\ImageProcessing-Matlab\Train\healthy\1.jpg');
function detect = infer(filename)
a = imread(filename);
load 'net.mat';

output = classify(net,a);

disp(class(output));
detect = output;
end