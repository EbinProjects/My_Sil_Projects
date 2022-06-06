package com.example.printingapp.bluetooth;


import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * For Image handling
 */

public class PrintUtil {


    public static byte[] getCommandPrintRasterBitImage(int width, int height) {

        byte[] PRINT_RASTER_BIT_IMAGE = new byte[]{(byte)29, (byte)118, (byte)48, (byte)0};

        int length = PRINT_RASTER_BIT_IMAGE.length + 4;
        byte offset = 0;
        byte[] command = new byte[length];
        System.arraycopy(PRINT_RASTER_BIT_IMAGE, 0, command, 0, PRINT_RASTER_BIT_IMAGE.length);
        int var11 = offset + PRINT_RASTER_BIT_IMAGE.length;
        int x = width / 8;
        if(width % 8 != 0) {
            ++x;
        }

        int xl = x % 256;
        int xh = x / 256;
        command[var11] = (byte)(xl & 255);
        ++var11;
        command[var11] = (byte)(xh & 255);
        ++var11;
        int yl = height % 256;
        int yh = height / 256;
        command[var11] = (byte)(yl & 255);
        ++var11;
        command[var11] = (byte)(yh & 255);
        return command;
    }

    public static Bitmap getMonoChromeImage(Bitmap bitmap)  {
        if(bitmap == null) {
            return null;
        } else {
            if(!bitmap.isMutable()) {
                bitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
            }

            try {
                int e = bitmap.getWidth();
                int height = bitmap.getHeight();
                int[] pixels = new int[e * height];
                bitmap.getPixels(pixels, 0, e, 0, 0, e, height);

                for(int YY = 0; YY < e; ++YY) {
                    for(int XX = 0; XX < height; ++XX) {
                        int bitmapColor = pixels[YY + XX * e];
                        int rr = Color.red(bitmapColor);
                        int gg = Color.green(bitmapColor);
                        int bb = Color.blue(bitmapColor);
                        int Y = (rr + gg + bb) / 3;
                        short X;
                        if(Y < 128) {
                            X = 0;
                        } else {
                            X = 255;
                        }

                        pixels[YY + XX * e] = Color.rgb(X, X, X);
                    }
                }

                bitmap.setPixels(pixels, 0, e, 0, 0, e, height);
                return bitmap;
            } catch (IllegalStateException var12) {
                return null;
            } catch (IllegalArgumentException var13) {
                return null;
            } catch (ArrayIndexOutOfBoundsException var14) {
                return null;
            }
        }
    }

    public static byte[] getRasterBitImage(Bitmap bitmap)  {
        if(bitmap == null) {
            return null;
        } else {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];

            try {
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            } catch (IllegalArgumentException var10) {
                return null;
            } catch (ArrayIndexOutOfBoundsException var11) {
                return null;
            }

            int x = width / 8;
            if(width % 8 != 0) {
                ++x;
            }

            int y = height;
            byte[] bitImage = new byte[x * height];
            int offset = 0;

            for(int index = 0; index < y; ++index) {
                byte[] rowBitData = createRowBitData(pixels, width, index);
                System.arraycopy(rowBitData, 0, bitImage, offset, rowBitData.length);
                offset += rowBitData.length;
            }

            return bitImage;
        }
    }

    private static byte[] createRowBitData(int[] data, int width, int row) {
        int size = width / 8;
        int surplus = width % 8;
        if(surplus != 0) {
            ++size;
        }

        byte[] bitImage = new byte[size];
        int offset = 0;
        int length;
        if(data.length >= width * (row + 1)) {
            length = width * (row + 1);
        } else {
            length = data.length;
        }

        length -= 7;
        int shift;
        if(surplus != 0) {
            shift = 8 - surplus;
        } else {
            shift = 0;
        }

        int index;
        for(index = width * row; index < length; index += 8) {
            byte bit = 0;
            if((data[index] & 16777215) == 0) {
                bit = (byte)(bit + 128);
            }

            if((data[index + 1] & 16777215) == 0) {
                bit = (byte)(bit + 64);
            }

            if((data[index + 2] & 16777215) == 0) {
                bit = (byte)(bit + 32);
            }

            if((data[index + 3] & 16777215) == 0) {
                bit = (byte)(bit + 16);
            }

            if((data[index + 4] & 16777215) == 0) {
                bit = (byte)(bit + 8);
            }

            if((data[index + 5] & 16777215) == 0) {
                bit = (byte)(bit + 4);
            }

            if((data[index + 6] & 16777215) == 0) {
                bit = (byte)(bit + 2);
            }

            if((data[index + 7] & 16777215) == 0) {
                ++bit;
            }

            bitImage[offset] = bit;
            ++offset;
        }

        if(shift > 0) {
            int var11 = 0;

            for(length += 7; index < length; ++index) {
                if((data[index] & 16777215) == 0) {
                    var11 = (var11 << 1) + 1;
                } else {
                    var11 <<= 1;
                }
            }

            var11 <<= shift;
            bitImage[offset] = (byte)(var11 & 255);
        }

        return bitImage;
    }
}
