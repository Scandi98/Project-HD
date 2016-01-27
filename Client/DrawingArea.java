// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public class DrawingArea extends NodeSub {
	
    public static void fillCircle(int x, int y, int radius, int color) {
        int y1 = y - radius;
        if (y1 < 0) {
            y1 = 0;
        }
        int y2 = y + radius;
        if (y2 >= height) {
            y2 = height - 1;
        }
        for (int iy = y1; iy <= y2; iy++) {
            int dy = iy - y;
            int dist = (int) Math.sqrt(radius * radius - dy * dy);
            int x1 = x - dist;
            if (x1 < 0) {
                x1 = 0;
            }
            int x2 = x + dist;
            if (x2 >= width) {
                x2 = width - 1;
            }
            int pos = x1 + iy * width;
            for (int ix = x1; ix <= x2; ix++) {
                pixels[pos++] = color;
            }
        }
    }
	

	public static void initDrawingArea(int i, int j, int ai[]) {
		pixels = ai;
		width = j;
		height = i;
		setDrawingArea(i, 0, j, 0);
	}

	public static void defaultDrawingAreaSize() {
		topX = 0;
		topY = 0;
		bottomX = width;
		bottomY = height;
		centerX = bottomX - 0;
		centerY = bottomX / 2;
	}

	public static void drawRect(int i, int j, int k, int l, int i1) {
		if (k < topX) {
			i1 -= topX - k;
			k = topX;
		}
		if (j < topY) {
			i -= topY - j;
			j = topY;
		}
		if (k + i1 > bottomX)
			i1 = bottomX - k;
		if (j + i > bottomY)
			i = bottomY - j;
		int k1 = width - i1;
		int l1 = k + j * width;
		for (int i2 = -i; i2 < 0; i2++) {
			for (int j2 = -i1; j2 < 0; j2++)
				pixels[l1++] = l;

			l1 += k1;
		}

	}

	public static void fillRect(int i, int j, int k, int l, int i1) {
		method339(i1, l, j, i);
		method339((i1 + k) - 1, l, j, i);
		method341(i1, l, k, i);
		method341(i1, l, k, (i + j) - 1);
	}

	public static void drawHorizontalLine(int drawX, int drawY, int lineWidth, int i_62_) {
		if (drawY >= topY && drawY < bottomY) {
			if (drawX < topX) {
				lineWidth -= topX - drawX;
				drawX = topX;
			}
			if (drawX + lineWidth > bottomX) {
				lineWidth = bottomX - drawX;
			}
			int i_63_ = drawX + drawY * width;
			for (int i_64_ = 0; i_64_ < lineWidth; i_64_++) {
				pixels[i_63_ + i_64_] = i_62_;
			}
		}
	}

	public static void method362(int var0, int var1, int var2, int var3, int var4, int var5) {
		if (var1 < topX) {
			if (var1 <= topX - var3) {
				return;
			}

			var3 -= topX - var1;
			var1 += topX - var1;
		}

		if (var1 + var3 > topY) {
			var3 -= var1 + var3 - topY;
		}

		if (var0 < topY) {
			var2 -= topY - var0;
			var0 = topY;
		}

		if (var0 + var2 > bottomX) {
			var2 = bottomX - var0;
		}

		for (int var6 = 0; var6 < var3; ++var6) {
			int var7 = var0 + (var1 + var6) * width;

			for (int var8 = 0; var8 < var2; ++var8) {
				int var9 = 256 - var5;
				int var10 = (var4 >> 16 & 255) * var5;
				int var11 = (var4 >> 8 & 255) * var5;
				int var12 = (var4 & 255) * var5;
				int var13 = (pixels[var7] >> 16 & 255) * var9;
				int var14 = (pixels[var7] >> 8 & 255) * var9;
				var9 = (pixels[var7] & 255) * var9;
				var9 = (var10 + var13 >> 8 << 16) + (var11 + var14 >> 8 << 8) + (var12 + var9 >> 8);
				pixels[var7++] = var9;
			}
		}

	}

	public static void setDrawingArea(int i, int j, int k, int l) {
		if (j < 0)
			j = 0;
		if (l < 0)
			l = 0;
		if (k > width)
			k = width;
		if (i > height)
			i = height;
		topX = j;
		topY = l;
		bottomX = k;
		bottomY = i;
		centerX = bottomX - 0;
		centerY = bottomX / 2;
		anInt1387 = bottomY / 2;
	}

	public static void setAllPixelsToZero() {
		int i = width * height;
		for (int j = 0; j < i; j++)
			pixels[j] = 0;

	}

	public static void method367(int var0, int var1, int var2, int var3, int var4, int var5) {
		if (var5 < topX) {
			var2 -= topX - var5;
			var5 = topX;
		}

		if (var1 < topY) {
			var3 -= topY - var1;
			var1 = topY;
		}

		if (var5 + var2 > bottomX) {
			var2 = bottomX - var5;
		}

		if (var1 + var3 > bottomX) {
			var3 = bottomX - var1;
		}

		int var6 = 256 - var4;
		int var7 = (var0 >> 16 & 255) * var4;
		int var8 = (var0 >> 8 & 255) * var4;
		var0 = (var0 & 255) * var4;
		var4 = width - var2;
		var1 = var5 + var1 * width;

		for (var5 = 0; var5 < var3; ++var5) {
			for (int var9 = -var2; var9 < 0; ++var9) {
				int var10 = (pixels[var1] >> 16 & 255) * var6;
				int var11 = (pixels[var1] >> 8 & 255) * var6;
				int var12 = (pixels[var1] & 255) * var6;
				var10 = (var7 + var10 >> 8 << 16) + (var8 + var11 >> 8 << 8) + (var0 + var12 >> 8);
				pixels[var1++] = var10;
			}

			var1 += var4;
		}

	}

	public static void method336(int i, int j, int k, int l, int i1) {
		if (k < topX) {
			i1 -= topX - k;
			k = topX;
		}
		if (j < topY) {
			i -= topY - j;
			j = topY;
		}
		if (k + i1 > bottomX)
			i1 = bottomX - k;
		if (j + i > bottomY)
			i = bottomY - j;
		int k1 = width - i1;
		int l1 = k + j * width;
		for (int i2 = -i; i2 < 0; i2++) {
			for (int j2 = -i1; j2 < 0; j2++)
				pixels[l1++] = l;

			l1 += k1;
		}

	}

	public static void method335(int i, int j, int k, int l, int i1, int k1) {// this
																				// function
																				// is
																				// that
																				// function
		if (k1 < topX) {
			k -= topX - k1;
			k1 = topX;
		}
		if (j < topY) {
			l -= topY - j;
			j = topY;
		}
		if (k1 + k > bottomX)
			k = bottomX - k1;
		if (j + l > bottomY)
			l = bottomY - j;
		int alpha = 256 - i1;
		int r = (i >> 16 & 0xff) * i1;
		int g = (i >> 8 & 0xff) * i1;
		int b = (i & 0xff) * i1;
		int k3 = width - k;
		int l3 = k1 + j * width;
		for (int i4 = 0; i4 < l; i4++) {
			for (int j4 = -k; j4 < 0; j4++) {
				int l2 = (pixels[l3] >> 16 & 0xff) * alpha;
				int i3 = (pixels[l3] >> 8 & 0xff) * alpha;
				int j3 = (pixels[l3] & 0xff) * alpha;
				int k4 = ((r + l2 >> 8) << 16) + ((g + i3 >> 8) << 8) + (b + j3 >> 8);
				pixels[l3++] = k4;
			}

			l3 += k3;
		}
	}

	// int i, int y, int x, int color, int width
	public static void drawPixels(int i, int j, int k, int rgb, int i1) {
		if (k < topX) {
			i1 -= topX - k;
			k = topX;
		}
		if (j < topY) {
			i -= topY - j;
			j = topY;
		}
		if (k + i1 > bottomX)
			i1 = bottomX - k;
		if (j + i > bottomY)
			i = bottomY - j;
		int k1 = width - i1;
		int l1 = k + j * width;
		for (int i2 = -i; i2 < 0; i2++) {
			for (int j2 = -i1; j2 < 0; j2++)
				pixels[l1++] = rgb;

			l1 += k1;
		}

	}

	public static void drawInnerShadow(int x, int y, int width, int height, int color, int alpha, int weight) {
		int fade_rate = alpha / weight;
		for (int index = 0; index < weight; index++) {
			method340(x + index, y + index, width - (index * 2), color, alpha - (index * fade_rate));
			method340(x + index, y + height - index, width - (index * 2), color, alpha - (index * fade_rate));
			method342(x + index, y + index + 1, height - (index * 2) - 1, color, alpha - (index * fade_rate));
			method342(x + width - index - 1, y + index + 1, height - (index * 2) - 1, color,
					alpha - (index * fade_rate));
		}
	}

	public static void drawDropShadow(int x, int y, int width, int height, int color, int alpha, int weight) {
		int fade_rate = alpha / weight;
		for (int index = 0; index < weight; index++) {
			method340(x - index, y - index, width + (index * 2), color, alpha - (index * fade_rate));
			method340(x - index, y + height + index, width + (index * 2), color, alpha - (index * fade_rate));
			method342(x - index, y - index + 1, height + (index * 2) - 1, color, alpha - (index * fade_rate));
			method342(x + width + index - 1, y - index + 1, height + (index * 2) - 1, color,
					alpha - (index * fade_rate));
		}
	}

	// do we want that? i was just looking at to fill in the box lol
	public static void fillPixels(int i, int j, int k, int l, int i1) {
		method339(i1, l, j, i);
		method339((i1 + k) - 1, l, j, i);
		method341(i1, l, k, i);
		method341(i1, l, k, (i + j) - 1);
	}

	public static void method338(int i, int j, int k, int l, int i1, int j1) {
		method340(l, i1, i, k, j1);
		method340(l, i1, (i + j) - 1, k, j1);
		if (j >= 3) {
			method342(l, j1, k, i + 1, j - 2);
			method342(l, (j1 + i1) - 1, k, i + 1, j - 2);
		}
	}

	public static void method339(int i, int j, int k, int l) {
		if (i < topY || i >= bottomY)
			return;
		if (l < topX) {
			k -= topX - l;
			l = topX;
		}
		if (l + k > bottomX)
			k = bottomX - l;
		int i1 = l + i * width;
		for (int j1 = 0; j1 < k; j1++)
			pixels[i1 + j1] = j;

	}

	public static void method340(int i, int j, int k, int l, int i1) {
		if (k < topY || k >= bottomY)
			return;
		if (i1 < topX) {
			j -= topX - i1;
			i1 = topX;
		}
		if (i1 + j > bottomX)
			j = bottomX - i1;
		int j1 = 256 - l;
		int k1 = (i >> 16 & 0xff) * l;
		int l1 = (i >> 8 & 0xff) * l;
		int i2 = (i & 0xff) * l;
		int i3 = i1 + k * width;
		for (int j3 = 0; j3 < j; j3++) {
			int j2 = (pixels[i3] >> 16 & 0xff) * j1;
			int k2 = (pixels[i3] >> 8 & 0xff) * j1;
			int l2 = (pixels[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
			pixels[i3++] = k3;
		}

	}

	public static void method341(int i, int j, int k, int l) {
		if (l < topX || l >= bottomX)
			return;
		if (i < topY) {
			k -= topY - i;
			i = topY;
		}
		if (i + k > bottomY)
			k = bottomY - i;
		int j1 = l + i * width;
		for (int k1 = 0; k1 < k; k1++)
			pixels[j1 + k1 * width] = j;

	}

	private static void method342(int i, int j, int k, int l, int i1) {
		if (j < topX || j >= bottomX)
			return;
		if (l < topY) {
			i1 -= topY - l;
			l = topY;
		}
		if (l + i1 > bottomY)
			i1 = bottomY - l;
		int j1 = 256 - k;
		int k1 = (i >> 16 & 0xff) * k;
		int l1 = (i >> 8 & 0xff) * k;
		int i2 = (i & 0xff) * k;
		int i3 = j + l * width;
		for (int j3 = 0; j3 < i1; j3++) {
			int j2 = (pixels[i3] >> 16 & 0xff) * j1;
			int k2 = (pixels[i3] >> 8 & 0xff) * j1;
			int l2 = (pixels[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
			pixels[i3] = k3;
			i3 += width;
		}
	}

	DrawingArea() {
	}

	public static int pixels[];
	public static int width;
	public static int height;
	public static int topY;
	public static int bottomY;
	public static int topX;
	public static int bottomX;
	public static int centerX;
	public static int centerY;
	public static int anInt1387;

}
