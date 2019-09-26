//****************************************************************************
// SketchBase.  
//****************************************************************************
// Comments : 
//   Subroutines to manage and draw points, lines an triangles
//
// History :
//   Aug 2014 Created by Jianming Zhang (jimmie33@gmail.com) based on code by
//   Stan Sclaroff (from CS480 '06 poly.c)

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SketchBase {
	
	public SketchBase()
	{
		// deliberately left blank
		
	}
	
	// draw a point
	
	
	public static void drawPoint(BufferedImage buff, Point2D p){
		buff.setRGB(p.x, buff.getHeight()-p.y-1, p.c.getBRGUint8());
	}
	
	//////////////////////////////////////////////////
	//	Implement the following two functions
	//////////////////////////////////////////////////
	
	
	
	public static ColorType colorInterpolation(int x, int y, Point2D p1, Point2D p2){
		float distancePercentage;
		float red, green, blue;
	
		boolean slopeUp;
		if((p1.y > p2.y && p1.x <= p2.x) || (p2.y > p1.y && p2.x <= p1.x)){
			slopeUp = true;
		} else {
			slopeUp = false;
		}

		if((slopeUp && (p1.y < p2.y) && x==p2.x && y==p2.y) || (!slopeUp && p1.y > p2.y && x==p2.x && y==p2.y)){
			System.out.println("case");
			return p2.c;
			
		} else {
			
			if(Math.abs(p1.x - p2.x) > Math.abs(p1.y - p2.y)){
				distancePercentage = (float) diff(x, p1.x) / diff(p1.x, p2.x);
			} 
			
			else{
				distancePercentage = (float) diff(y, p1.y) / diff(p1.y, p2.y);
			}
		}
		 
		red = p1.c.r*(1 - distancePercentage) + p2.c.r*distancePercentage;
		green = p1.c.g*(1 - distancePercentage) + p2.c.g*distancePercentage;
		blue = p1.c.b*(1 - distancePercentage) + p2.c.b*distancePercentage;	
		
		
			
		ColorType color = new ColorType(red, green, blue);
		return color;
	}
	
	public static int diff(int x, int y){
		return Math.abs(x - y);
	}
	
	
	
	
	public static void drawLine(BufferedImage buff, Point2D p1, Point2D p2, boolean doSmooth){
		if(doSmooth){
			
			int xDiff = Math.abs(p1.x - p2.x);
			int yDiff = Math.abs(p1.y - p2.y);
			
			// slopeUp is needed to figure out whether we should increment or decrement y while following along the x axis
			// highSlope is a boolean that indicated that we should step along the x axis when the slope is < 1 and along y axis when it is greater than 1
			boolean slopeUp, highSlope; 
			
			int x, y; //for current point 
			int xEnd, yEnd;
			int decisionParam;
			
			ColorType color0;
			ColorType colorEnd;
			
			
			//determine which point is left-most and which one is the right-most
			if(p1.x > p2.x){
				x = p2.x;
				y = p2.y;
				xEnd = p1.x;
				yEnd = p1.y;
				
				if(p1.y > p2.y){
					slopeUp = true;
				}
				else{
					slopeUp = false;
				}
				
				color0 = p2.c;
				colorEnd = p1.c;
		
				
			} else{
				x = p1.x;
				y = p1.y;
				xEnd = p2.x;
				yEnd = p2.y;
				
				if(p1.y > p2.y){
					slopeUp = false;
				}
				else{
					slopeUp = true;
				}
					
				color0 = p1.c;
				colorEnd = p2.c;
			}
			
			if((yDiff - xDiff) > 0){
				highSlope = true;
			} else{
				highSlope = false;
			}
			
			
			Point2D firstPoint = new Point2D(x, y, color0);
			
			drawPoint(buff, firstPoint);
			
			if(!highSlope){
				decisionParam = 2*yDiff - xDiff;
				while(x < xEnd+1){
					x++;
					if(decisionParam < 0){
						decisionParam += 2*yDiff;
					}
					else{
						if(slopeUp){
							y++;
						} else{
							y--;
						}
						decisionParam = decisionParam + 2*yDiff - 2*xDiff;
					}

					ColorType currentColor = colorInterpolation(x, y, p1, p2);
					
					Point2D currentPoint = new Point2D(x, y, currentColor);
					drawPoint(buff, currentPoint);
				}
			}
			
			else{ // high slope
				decisionParam = 2*xDiff - yDiff;
				if(slopeUp){
					while(y < yEnd+1){
						y++;
						if(decisionParam < 0){
							decisionParam += 2*xDiff;  
						}
						else{
							x++;
							decisionParam = decisionParam + 2*xDiff - 2*yDiff;
						}
						
						// getting the new RGB values 
						ColorType currentColor = colorInterpolation(x, y, p1, p2);
						Point2D currentPoint = new Point2D(x, y, currentColor);
						drawPoint(buff, currentPoint);
						
					}
				}
				
				else{
					while(y > yEnd){
						y--;
						if(decisionParam < 0){
							decisionParam += 2*xDiff;   //check 
						}
						else{
							x++;
							decisionParam = decisionParam + 2*xDiff - 2*yDiff;
						}
						
						ColorType currentColor = colorInterpolation(x, y, p1, p2);
						Point2D currentPoint = new Point2D(x, y, currentColor);
						drawPoint(buff, currentPoint);
					}	
				}	
			}
			 
			
			
		} // DO NOT SMOOTH 
		else{
			
			int xDiff = Math.abs(p1.x - p2.x);
			int yDiff = Math.abs(p1.y - p2.y);
			
			// slopeUp is needed to figure out whether we should increment or decrement y while following along the x axis
			// highSlope is a boolean that indicated that we should step along the x axis when the slope is < 1 and along y axis when it is greater than 1
			boolean slopeUp, highSlope; 
		
			int x, y; //for current point 

			int xEnd, yEnd;
			int decisionParam;
						
			//determine which point is left-most and which one is the right-most
			if(p1.x > p2.x){
				x = p2.x;
				y = p2.y;
				xEnd = p1.x;
				yEnd = p1.y;
				
				if(p1.y > p2.y){
					slopeUp = true;
				}
				else{
					slopeUp = false;
				}
				
			} else{
				x = p1.x;
				y = p1.y;
				xEnd = p2.x;
				yEnd = p2.y;
				
				if(p1.y > p2.y){
					slopeUp = false;
				}
				else{
					slopeUp = true;
				}

			}
			
			if((yDiff - xDiff) > 0){
				highSlope = true;
			} else{
				highSlope = false;
			}
			
			
			Point2D firstPoint = new Point2D(x, y, p1.c);
			
			drawPoint(buff, firstPoint);
			
			if(!highSlope){
				decisionParam = 2*yDiff - xDiff;
				while(x < xEnd){
					x++;
					if(decisionParam < 0){
						decisionParam += 2*yDiff;
					}
					else{
						if(slopeUp){
							y++;
						} else{
							y--;
						}
						decisionParam = decisionParam + 2*yDiff - 2*xDiff;
					}
					
					Point2D currentPoint = new Point2D(x, y, p1.c);
					drawPoint(buff, currentPoint);
				}
			}
			
			else{
				decisionParam = 2*xDiff - yDiff;
				if(slopeUp){
					while(y < yEnd){
						y++;
						if(decisionParam < 0){
							decisionParam += 2*xDiff;  
						}
						else{
							x++;
							decisionParam = decisionParam + 2*xDiff - 2*yDiff;
						}

						Point2D currentPoint = new Point2D(x, y, p1.c);
						drawPoint(buff, currentPoint);
						
					}
				}
				
				else{
					while(y > yEnd){
						y--;
						if(decisionParam < 0){
							decisionParam += 2*xDiff;  
						}
						else{
							x++;
							decisionParam = decisionParam + 2*xDiff - 2*yDiff;
						}

						Point2D currentPoint = new Point2D(x, y, p1.c);
						drawPoint(buff, currentPoint);
					}
					
				}	
				
			}
			
		}
		
	}
	
	
	// draw a triangle
	public static void drawTriangle(BufferedImage buff, Point2D p1, Point2D p2, Point2D p3, boolean doSmooth){
		Point2D middlePointY, middlePointX, lowerPoint, otherPoint, vertex1, vertex2;
		if((p1.y <= p2.y && p1.y >= p3.y) || (p1.y >= p2.y && p1.y <= p3.y)){
			middlePointY = p1;
		} else if((p2.y <= p3.y && p2.y >= p1.y) || (p2.y >= p3.y && p2.y <= p1.y)){
			middlePointY = p2;
		} else{
			middlePointY = p3;
		}
		
		if((p1.x <= p2.x && p1.x >= p3.x) || (p1.x >= p2.x && p1.x <= p3.x)){
			middlePointX = p1;
		} else if((p2.x <= p3.x && p2.x >= p1.x) || (p2.x >= p3.x && p2.x <= p1.x)){
			middlePointX = p2;
		} else{
			middlePointX = p3;
		}
		
		if(p1.y >= p2.y && p1.y >= p3.y){
			lowerPoint = p1;
		} else if(p2.y >= p1.y && p2.y >= p3.y){
			lowerPoint = p2;
		} else{
			lowerPoint = p3;
		}
		
	 //in order to fill a triangle using bilinear interpolation, we have to split it upper and lower triangles, 
		//both having 1 side parallel to one of the axis. Let it be x axis. Let us find the proportions in which the line that goes through the middlePointY and 
		//that is parallel to x axis, is splitting the other edge of the triangle: 
		
		Point2D centerPoint = new Point2D(middlePointX.x, middlePointY.y, p1.c);

		boolean equalMids = false; // when midPointX = midPointY
		if(middlePointX.x == middlePointY.x && middlePointX.y == middlePointY.y){
			equalMids = true;
		}
		
		int y = centerPoint.y;
		int leftX, rightX;
		Point2D leftFillPoint, rightFillPoint;
		
		ColorType color = p1.c;
		
		if(!doSmooth){
			
			if(!equalMids){
				//the vertex than is not the middle one either in alignment with x or y axis 
				if(p1.y != middlePointX.y && p1.y != middlePointY.y && p1.x != middlePointX.x && p1.x != middlePointY.x){
					otherPoint = p1;
				} else if (p2.y != middlePointX.y && p2.y != middlePointY.y && p2.x != middlePointX.x && p2.x != middlePointY.x){
					otherPoint = p2;
				} else{
					otherPoint = p3;
				}
				
				//interpolating to get point a
				int newX = (y - otherPoint.y)*(middlePointX.x - otherPoint.x)/(middlePointX.y - otherPoint.y) + otherPoint.x; 
				Point2D a = new Point2D(newX, y, new ColorType(0,1,0)); 
				drawLine(buff, middlePointY, a, false);
				
				y = centerPoint.y;
				while(y != middlePointX.y){
					if(y < middlePointX.y){
						y++;
					}
					else{
						y--;
					}
					leftX = (y - a.y)*(middlePointX.x - a.x)/(middlePointX.y - a.y) + a.x; 
					rightX = (y - middlePointY.y)*(middlePointX.x - middlePointY.x)/(middlePointX.y - middlePointY.y) + middlePointY.x;
					leftFillPoint = new Point2D(leftX,y,color);
					rightFillPoint = new Point2D(rightX,y, color);
					drawLine(buff, leftFillPoint, rightFillPoint, false);
				
				}
				
				y = centerPoint.y;
				while(y != otherPoint.y){
					if(y < otherPoint.y){
						y++;
					}
					else{
						y--;
					}
					leftX = (y - a.y)*(otherPoint.x - a.x)/(otherPoint.y - a.y) + a.x; 
					rightX = (y - middlePointY.y)*(otherPoint.x - middlePointY.x)/(otherPoint.y - middlePointY.y) + middlePointY.x;
					leftFillPoint = new Point2D(leftX,y,color);
					rightFillPoint = new Point2D(rightX,y, color);
					drawLine(buff, leftFillPoint, rightFillPoint, false);
				
				}
			}
			
			else{ // when midPointX = midPointY
				
				if(p1.y != middlePointX.y && p1.y != middlePointY.y && p2.y != middlePointX.y && p2.y != middlePointY.y){
					vertex1 = p1;
					vertex2 = p2;
				} else if (p1.y != middlePointX.y && p1.y != middlePointY.y && p3.y != middlePointX.y && p3.y != middlePointY.y){
					vertex1 = p1;
					vertex2 = p3;
				} else {
					vertex1 = p2;
					vertex2 = p3;
				} //p2.y != middlePointX.y && p2.y != middlePointY.y && p3.y != middlePointX.y && p3.y != middlePointY.y
					
				int newX = (y - vertex1.y)*(vertex2.x - vertex1.x)/(vertex2.y - vertex1.y) + vertex1.x; 
				Point2D a = new Point2D(newX, y, new ColorType(0,1,0));
				drawLine(buff, middlePointY, a, false);
				
				while(y != vertex2.y){
					if(y < vertex2.y){
						y++;
					}
					else{
						y--;
					}
					leftX = (y - a.y)*(vertex2.x - a.x)/(vertex2.y - a.y) + a.x; 
					rightX = (y - middlePointY.y)*(vertex2.x - middlePointY.x)/(vertex2.y - middlePointY.y) + middlePointY.x;
					leftFillPoint = new Point2D(leftX,y,color);
					rightFillPoint = new Point2D(rightX,y, color);
					drawLine(buff, leftFillPoint, rightFillPoint, false);
				
				}
				y = centerPoint.y;
				while(y != vertex1.y){
					if(y < vertex1.y){
						y++;
					}
					else{
						y--;
					}
					leftX = (y - a.y)*(vertex1.x - a.x)/(vertex1.y - a.y) + a.x; 
					rightX = (y - middlePointY.y)*(vertex1.x - middlePointY.x)/(vertex1.y - middlePointY.y) + middlePointY.x;
					leftFillPoint = new Point2D(leftX,y,color);
					rightFillPoint = new Point2D(rightX,y, color);
					drawLine(buff, leftFillPoint, rightFillPoint, false);
				
				}
				
			}
			
		} else{ // DO SMOOTH
			if(!equalMids){
				//the vertex than is not the middle one either in alignment with x or y axis 
				if(p1.y != middlePointX.y && p1.y != middlePointY.y && p1.x != middlePointX.x && p1.x != middlePointY.x){
					otherPoint = p1;
				} else if (p2.y != middlePointX.y && p2.y != middlePointY.y && p2.x != middlePointX.x && p2.x != middlePointY.x){
					otherPoint = p2;
				} else{
					otherPoint = p3;
				}

				//interpolating to get point a and its color
				int newX = (y - otherPoint.y)*(middlePointX.x - otherPoint.x)/(middlePointX.y - otherPoint.y) + otherPoint.x; 
				ColorType colorA = colorInterpolation(newX, y, middlePointX, otherPoint);
				Point2D a = new Point2D(newX, y, colorA);

				drawLine(buff, middlePointY, a, true);
				
				while(y != middlePointX.y){
					if(y < middlePointX.y){
						y++;
					}
					else{
						y--;
					}	
					
					leftX = (y - a.y)*(middlePointX.x - a.x)/(middlePointX.y - a.y) + a.x; 
					color = colorInterpolation(leftX, y, a, middlePointX);
					leftFillPoint = new Point2D(leftX,y,color);
					
					rightX = (y - middlePointY.y)*(middlePointX.x - middlePointY.x)/(middlePointX.y - middlePointY.y) + middlePointY.x;
					color = colorInterpolation(rightX, y, middlePointY, middlePointX);
					rightFillPoint = new Point2D(rightX,y, color);

					drawLine(buff, leftFillPoint, rightFillPoint, true);
				
				}
				y = centerPoint.y;
				while(y != otherPoint.y){
					if(y < otherPoint.y){
						y++;
					}
					else{
						y--;
					}
					
					leftX = (y - a.y)*(otherPoint.x - a.x)/(otherPoint.y - a.y) + a.x; 
					color = colorInterpolation(leftX, y, a, otherPoint);
					leftFillPoint = new Point2D(leftX,y,color);
					
					rightX = (y - middlePointY.y)*(otherPoint.x - middlePointY.x)/(otherPoint.y - middlePointY.y) + middlePointY.x;
					color = colorInterpolation(rightX, y, middlePointY, otherPoint);
					rightFillPoint = new Point2D(rightX,y, color);
					drawLine(buff, leftFillPoint, rightFillPoint, true);
				}
				
			} 
			
			else{  // when midPointX = midPointY
				
				if(p1.y != middlePointX.y && p1.y != middlePointY.y && p2.y != middlePointX.y && p2.y != middlePointY.y){
					vertex1 = p1;
					vertex2 = p2;
				} else if (p1.y != middlePointX.y && p1.y != middlePointY.y && p3.y != middlePointX.y && p3.y != middlePointY.y){
					vertex1 = p1;
					vertex2 = p3;
				} else {
					vertex1 = p2;
					vertex2 = p3;
				} 
					
				int newX = (y - vertex1.y)*(vertex2.x - vertex1.x)/(vertex2.y - vertex1.y) + vertex1.x; 
				color = colorInterpolation(newX, y, vertex2, vertex1);
				Point2D a = new Point2D(newX, y, color);
				drawLine(buff, middlePointY, a, true);
				
				while(y != vertex2.y){
					if(y < vertex2.y){
						y++;
					}
					else{
						y--;
					}
					leftX = (y - a.y)*(vertex2.x - a.x)/(vertex2.y - a.y) + a.x; 
					color = colorInterpolation(leftX, y, a, vertex2);
					leftFillPoint = new Point2D(leftX,y,color);
					
					rightX = (y - middlePointY.y)*(vertex2.x - middlePointY.x)/(vertex2.y - middlePointY.y) + middlePointY.x;
					color = colorInterpolation(rightX, y, vertex2, middlePointY);
					rightFillPoint = new Point2D(rightX,y, color);
					
					drawLine(buff, leftFillPoint, rightFillPoint, true);
				
				}
				y = centerPoint.y;
				while(y != vertex1.y){
					if(y < vertex1.y){
						y++;
					}
					else{
						y--;
					}
					leftX = (y - a.y)*(vertex1.x - a.x)/(vertex1.y - a.y) + a.x; 
					color = colorInterpolation(leftX, y, a, vertex1);
					leftFillPoint = new Point2D(leftX,y,color);
					
					
					rightX = (y - middlePointY.y)*(vertex1.x - middlePointY.x)/(vertex1.y - middlePointY.y) + middlePointY.x;
					color = colorInterpolation(rightX, y, middlePointY, vertex1);
					rightFillPoint = new Point2D(rightX,y, color);
					drawLine(buff, leftFillPoint, rightFillPoint, true);
				
				}

			}			
			
		}
			
	}
	
	/////////////////////////////////////////////////
	// for texture mapping (Extra Credit for CS680)
	/////////////////////////////////////////////////
	public static void triangleTextureMap(BufferedImage buff, BufferedImage texture, Point2D p1, Point2D p2, Point2D p3)
	{
		// replace the following line with your implementation
		drawLine(buff, p1, p2, false);
		drawLine(buff, p2, p3, false);
		drawLine(buff, p3, p1, false);
	}
	
}
