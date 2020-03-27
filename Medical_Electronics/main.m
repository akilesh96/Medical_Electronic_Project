


%%%%%%READ THE IMAGES FROM THE DATASET %%%%%%%%

% [filename,pathname] = uigetfile('*.*');
function cell_detected = main(pathname,filename)
cell_detected = 0;

im = imread(filename);

figure,imshow(im),title('INPUT IMAGE');
saveas(gcf,strcat(pathname,'\','input.png'));


%%COLOR SPACE CONVERSION FROM RGB TO HSV%%

[h,s,v] = rgb2hsv(im);

figure,

subplot(131),imshow(h);title('Hue');

subplot(132),imshow(s);title('Saturation');

subplot(133),imshow(v);title('Value');

set(gcf, 'Position', get(0,'Screensize'));
saveas(gcf,strcat(pathname,'\','HSV.png'));

%%%%%%%% CHOOSE S-CHANNEL IMAGE AND CONVERT TO BLACK AND WHITE IMAGE
%%%%%%%% %%%%%%%

scbw=im2bw(s,0.90);

figure,imshow(scbw),title('S-CHANNEL BLACK AND WHITE IMAGE');
saveas(gcf,strcat(pathname,'\','black_and_white.png'));

%%%%%%%TO REMOVE THE UNWANTED BORDERS %%%%%%%%

bor=imclearborder(scbw);

figure,imshow(bor),title('BORDER CORRECTED IMAGE');
saveas(gcf,strcat(pathname,'\','border_corrected.png'));

%%%%%%% FILL THE HOLES %%%%%% 

ho=imfill(bor,'holes');

figure,imshow(ho),title('HOLES FILLED IMAGE');
saveas(gcf,strcat(pathname,'\','holes_filled.png'));

%%%%%% TO DILATE THE IMAGE %%%%%%%%

SE=strel('diamond',6);

di=imdilate(ho,SE);

figure,imshow(di),title('DILATED IMAGE');
saveas(gcf,strcat(pathname,'\','dilated.png'));

%%%%%%AGAIN FILL THE HOLES %%%%%

hooo=imfill(di,'holes');

figure,imshow(hooo),title('HOLES FILLED IMAGE');
saveas(gcf,strcat(pathname,'\','holes_filled2.png'));

%%%%%APPLY REGION PROPERTIES %%%%%%

stats=regionprops(hooo,'all');

area=[stats.Area];




%%%%%%%COUNT THE NUMBER OF INDEPENDENT OBJECTS %%%%%%%

f1=bwconncomp(hooo);

a1=f1.NumObjects;

%%%%%%APPLY CONDITION USING IF LOOP %%%%%%%

if(a1==0)
    
    figure,imshow(im,[]),title('INPUT IMAGE');
    saveas(gcf,strcat(pathname,'\','output.png'));
    
%     msgbox('NO CELL DETECTED');
    
    
elseif(a1<2)
    
    hooo=bwareaopen(hooo,150);
    
    SE1=strel('diamond',2);
    
    hooo=imdilate(hooo,SE1);
    
    figure,imshow(hooo);
    
    
    figure,imshow(im);title('INPUT IMAGE');

hold on
%% Segmentation
S=regionprops(hooo,'All');
for i=1:length(S)
box_temp1 =S(i).BoundingBox;
a = box_temp1(1) + (box_temp1(3)/2);
b = box_temp1(2) + (box_temp1(4)/2);
Elong(i) = 1 -(box_temp1(3)/box_temp1(4));
center1_temp = S(i).Centroid;
viscircles([a,b],40);

end

% if(area<140)
% %msgbox('NO CELL DETECTED');
% 
% else
%       msgbox('CELL DETECTED');
% end


hold off
saveas(gcf,strcat(pathname,'\','output.png'));  
cell_detected = length(S);
disp('Total number of  cells detected in image = '); 
disp(cell_detected)

    
elseif(a1>=7)
    
    hooot=bwareaopen(hooo,1000);
    
    figure,imshow(hooot);
    
    f1=bwconncomp(hooot);

a111=f1.NumObjects;

if(a111==1)
     
    figure,imshow(im);title('INPUT IMAGE');
    
hold on
%% Segmentation
S2=regionprops(hooot,'All');
for i=1:length(S2)
box_temp1 =S2(i).BoundingBox;
aa = box_temp1(1) + (box_temp1(3)/2);
bb = box_temp1(2) + (box_temp1(4)/2);
Elong(i) = 1 -(box_temp1(3)/box_temp1(4));
center1_temp = S2(i).Centroid;
viscircles([aa,bb],40);
end
hold off
saveas(gcf,strcat(pathname,'\','output.png'));
%  msgbox('CELL DETECTED'); 
 
cell_detected = length(S2);
disp('Total number of cells detected in image = '); 
disp(cell_detected)

else
    figure,imshow(im),title('INPUT IMAGE');
    saveas(gcf,strcat(pathname,'\','output.png'));
    
%     msgbox('NO CELL DETECTED');
end

elseif(a1<7&&a1>=2)
    
    hoooo=bwareaopen(hooo,500);
     figure,imshow(hoooo);
     stats1=regionprops(hoooo,'all');
     area=[stats.Area];
 
       hoooo=imclearborder(hoooo);
       figure,imshow(hoooo);
       
       hoooo=imdilate(hoooo,1);
       figure,imshow(hoooo);
       
       hh=bwareaopen(hoooo,150);
       figure,imshow(hh);
       
       SE1=strel('diamond',3);
    hh=imdilate(hh,SE1);
    figure,imshow(hh);
    
       f2=bwconncomp(hh);

a2=f2.NumObjects;

if(a2==0)
    
    figure,imshow(im),title('INPUT IMAGE');
    saveas(gcf,strcat(pathname,'\','output.png'));
%     msgbox('NO CELL DETECTED');
    
end

if(a2>=6)
    
    figure,imshow(im),title('INPUT IMAGE');
    saveas(gcf,strcat(pathname,'\','output.png'));
%     msgbox('NO CELL DETECTED');
    
elseif(a2<6&&a2>=1)
    
    figure,imshow(im);title('INPUT IMAGE');
hold on
%% Segmentation
S1=regionprops(hh,'All');
for i=1:length(S1)
box_temp1 =S1(i).BoundingBox;
a11 = box_temp1(1) + (box_temp1(3)/2);
b11 = box_temp1(2) + (box_temp1(4)/2);
Elong(i) = 1 -(box_temp1(3)/box_temp1(4));
center1_temp = S1(i).Centroid;
viscircles([a11,b11],40);
end

  hold off
  saveas(gcf,strcat(pathname,'\','output.png'));
%   msgbox('CELL DETECTED');
  
cell_detected = length(S1);
disp('Total number of  cells detected in image = '); 
disp(cell_detected)
  
end
end
close all

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
