package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import characters.Animation;
import characters.AnimationStep;
import display.Sprite;
import display.Window;
import physics.CollisionAreas;
import physics.CollisionBox;
import physics.Vector;

public class AnimationEditor {
	
	static List<Animation> animations;

	static JFrame frame;
	static JPanel previewWindow;
	static JPanel animsPanel;
	static JScrollPane animsScroller;
	static JList<Animation> anims;
	static DefaultListModel<Animation> animModel;
	static JButton addAnim;
	static JButton deleteAnim;
	static JCheckBox repeat;
	static JTextField name;
	static JPanel stepPanel;
	static JScrollPane stepScroller;
	static JList<AnimationStep> steps;
	static DefaultListModel<AnimationStep> stepModel;
	static JButton addStep;
	static JButton deleteStep;
	static JSlider angle;
	static JSpinner frameCount;
	static JCheckBox interuptable;
	static JCheckBox specialCancelable;
	static JCheckBox preview;
	static JPanel cBoxesPanel;
	static JScrollPane cBoxesScroller;
	static JList<CollisionBox> cBoxes;
	static DefaultListModel<CollisionBox> boxModel;
	static JButton addBox;
	static JButton deleteBox;
	static JSlider rotation;
	static JTextField boxName;
	static JSlider boxX;
	static JSlider boxY;
	static JSlider boxW;
	static JSlider boxH;
	static JButton color;
	static JCheckBox hitbox;
	static JSpinner damage;
	static JFileChooser imagePicker;
	static Color currColor;
	static JSpinner trajectoryX;
	static JSpinner trajectoryY;
	static JButton load;
	static JButton save;
	static JCheckBox hitInvincible;
	static JCheckBox normalInvincible;
	static JCheckBox grabInvincible;
	static JCheckBox projectileInvincible;
	static JSpinner armorAmount;
	static JSpinner hitstun;
	static JCheckBox knockdown;
	static JSpinner groundLocation;
	static JCheckBox setVelocity;
	static JSpinner velocityX;
	static JSpinner velocityY;
	static JCheckBox ignoresGravity;
	
	
	public static void main(String args[]) {
		frame = new JFrame();
		frame.getContentPane().setLayout(new GridLayout(1, 4));
		
		animsPanel = new JPanel(new BorderLayout());
		animModel = new DefaultListModel<>();
		anims = new JList<>(animModel);
		anims.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		anims.addListSelectionListener(new ListSelectionListener() {
            @Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					if (anims.isSelectionEmpty()) {
						resetAnimVals();
					} else {
						selectAnim(anims.getSelectedValue());
					}
				}
			}
		});
		animsScroller = new JScrollPane(anims);
		animsPanel.add(animsScroller, BorderLayout.CENTER);
		JPanel p3 = new JPanel(new GridLayout(0, 2));
		save = new JButton("Save");
		save.setToolTipText("Save animations to file");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Animation files", "anims");
				imagePicker.setFileFilter(filter);
				int returnVal = imagePicker.showSaveDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File f = imagePicker.getSelectedFile();
					if (!f.getName().endsWith(".anims")) {
						f = new File(f.getAbsolutePath() + ".anims");
					}
					saveAnimations(f);
					frame.setTitle("Editing: " + imagePicker.getSelectedFile().getName());
				}
			}
		});
		p3.add(save);
		load = new JButton("Load");
		load.setToolTipText("Load animations from file");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Animation files", "anims");
				imagePicker.setFileFilter(filter);
				int returnVal = imagePicker.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					animModel.clear();
					resetAnimVals();
					Map<String, Animation> animMap = Animation.loadAnimations(imagePicker.getSelectedFile(), null);
					for (Animation a : animMap.values()) {
						animModel.addElement(a);
					}
					if (animModel.size() > 0) {
						anims.setSelectedValue(animModel.getElementAt(0), true);
						selectAnim(animModel.getElementAt(0));
					}
					frame.setTitle("Editing: " + imagePicker.getSelectedFile().getName());
				}
			}
		});
		p3.add(load);
		animsPanel.add(p3, BorderLayout.NORTH);
		
		JPanel p = new JPanel(new GridLayout(0, 2));
		addAnim = new JButton("+");
		addAnim.setToolTipText("Add animation");
		addAnim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(frame, "Name of animation");
				if (name == null) {
					return;
				}
				int ground = (int) groundLocation.getValue();
				if (!animModel.isEmpty() && animModel.getElementAt(animModel.size() - 1) != null) {
					ground = animModel.getElementAt(animModel.size() - 1).getGroundLocation();
				}
				animModel.addElement(new Animation(name, repeat.isSelected(), ground,
						new ArrayList<AnimationStep>()));
				anims.setSelectedIndex(animModel.size() - 1);
				selectAnim(anims.getSelectedValue());
			}
		});
		p.add(addAnim);
		deleteAnim = new JButton("-");
		deleteAnim.setToolTipText("Delete animation");
		deleteAnim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int newSelection = anims.getSelectedIndex() - 1;
				animModel.remove(anims.getSelectedIndex());
				if (animModel.size() > 0) {
					if (newSelection < 0) {
						newSelection = 0;
					}
					anims.setSelectedValue(animModel.getElementAt(newSelection), true);
					selectAnim(animModel.getElementAt(newSelection));
				} else {
					resetAnimVals();
				}
			}
		});
		p.add(deleteAnim);
		name = new JTextField("Name");
		name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!anims.isSelectionEmpty()) {
					anims.getSelectedValue().setString(name.getText());
					anims.repaint();
				}
			}
		});
		p.add(name);
		repeat = new JCheckBox("Repeat");
		repeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!anims.isSelectionEmpty()) {
					anims.getSelectedValue().setRepeat(repeat.isSelected());
				}
			}
		});
		p.add(repeat);
		groundLocation = new JSpinner(new SpinnerNumberModel(200, 0, 500, 1));
		groundLocation.setToolTipText("Y Coordinate of the ground");
		groundLocation.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!anims.isSelectionEmpty()) {
					anims.getSelectedValue().setGroundLocation((int) groundLocation.getValue());
				}
			}
		});
		p.add(groundLocation);
		preview = new JCheckBox("Preview");
		preview.addActionListener(new ActionListener() {
			boolean savedVal;
			
			public void actionPerformed(ActionEvent e) {
				if (preview.isSelected()) {
					savedVal = anims.getSelectedValue().repeat();
					anims.getSelectedValue().setRepeat(true);
				} else {
					anims.getSelectedValue().setRepeat(savedVal);
				}
			}
		});
		p.add(preview);
		animsPanel.add(p, BorderLayout.SOUTH);
		frame.getContentPane().add(animsPanel);
		
		stepPanel = new JPanel(new BorderLayout());
		stepModel = new DefaultListModel<>();
		steps = new JList<>(stepModel);
		steps.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		steps.addListSelectionListener(new ListSelectionListener() {
            @Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					if (steps.isSelectionEmpty()) {
						resetStepVals();
					} else {
						selectStep(steps.getSelectedValue());
					}
				}
			}
		});
		stepScroller = new JScrollPane(steps);
		stepPanel.add(stepScroller, BorderLayout.CENTER);
		JPanel p2 = new JPanel(new GridLayout(0, 2));
		addStep = new JButton("+");
		addStep.setToolTipText("Add animation step");
		imagePicker = new JFileChooser("./sprites");
		addStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (anims.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(frame, "You need to select an animation first!");
					return;
				}
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
				imagePicker.setFileFilter(filter);
				int returnVal = imagePicker.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					List<CollisionBox> boxes = new ArrayList<>();
					if (!steps.isSelectionEmpty()) {
						for (CollisionBox b : getSelectedStep().getCollisions().getBoxes()) {
							boxes.add(new CollisionBox(b.toString(), b.getColor(), b.getX(), b.getY(), b.getWidth(),
									b.getHeight(), b.getAngle(), false, b.getDamage(), b.getHitstunFrames(),
									b.knocksDown(), b.getTrajectory(false)));
						}
					}
					Double f3 = (Double) velocityX.getValue();
					Double f4 = (Double) velocityY.getValue();
					anims.getSelectedValue().getSteps()
							.add(new AnimationStep(new Sprite(imagePicker.getSelectedFile().getAbsolutePath()),
									interuptable.isSelected(), specialCancelable.isSelected(),
									(int) frameCount.getValue(), hitInvincible.isSelected(),
									normalInvincible.isSelected(), grabInvincible.isSelected(),
									projectileInvincible.isSelected(), (int) armorAmount.getValue(),
									ignoresGravity.isSelected(),
									new Vector(f3.floatValue(), f4.floatValue()),
									setVelocity.isSelected(), new CollisionAreas(boxes)));
					stepModel.addElement(
							anims.getSelectedValue().getSteps().get(anims.getSelectedValue().getSteps().size() - 1));
					steps.setSelectedIndex(stepModel.size() - 1);
					selectStep(steps.getSelectedValue());
				}
			}
		});
		p2.add(addStep);
		deleteStep = new JButton("-");
		deleteStep.setToolTipText("Delete animation step");
		deleteStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!steps.isSelectionEmpty()) {
					int newSelection = steps.getSelectedIndex() - 1;
					anims.getSelectedValue().getSteps().remove(steps.getSelectedIndex());
					stepModel.remove(steps.getSelectedIndex());
					if (stepModel.size() > 0) {
						if (newSelection < 0) {
							newSelection = 0;
						}
						steps.setSelectedValue(stepModel.getElementAt(newSelection), true);
						selectStep(stepModel.getElementAt(newSelection));
					} else {
						resetStepVals();
					}
				}
			}
		});
		p2.add(deleteStep);
		interuptable = new JCheckBox("Interuptable");
		interuptable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!steps.isSelectionEmpty()) {
					steps.getSelectedValue().setInteruptable(interuptable.isSelected());
				}
			}
		});
		p2.add(interuptable);
		specialCancelable = new JCheckBox("Special Cancelable");
		specialCancelable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!steps.isSelectionEmpty()) {
					steps.getSelectedValue().setSpecialInteruptable(specialCancelable.isSelected());
				}
			}
		});
		p2.add(specialCancelable);
		frameCount = new JSpinner(new SpinnerNumberModel(Animation.FRAMES_PER_SPRITE, 1, 6000, 1));
		frameCount.setToolTipText("Frames this step lasts");
		frameCount.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!steps.isSelectionEmpty()) {
					steps.getSelectedValue().setFramesToDisplay((int) frameCount.getValue()); 
				}
			}
		});
		p2.add(frameCount);
		armorAmount = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		armorAmount.setToolTipText("Number of hits of armor");
		armorAmount.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!steps.isSelectionEmpty()) {
					getSelectedStep().setArmorAmount((int) armorAmount.getValue());
				}
			}
		});
		p2.add(armorAmount);
		hitInvincible = new JCheckBox("Hit Invincible");
		hitInvincible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!steps.isSelectionEmpty()) {
					getSelectedStep().setHitInvincible(hitInvincible.isSelected());
				}
			}
		});
		p2.add(hitInvincible);
		normalInvincible = new JCheckBox("Normal Invincible");
		normalInvincible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!steps.isSelectionEmpty()) {
					getSelectedStep().setNormalInvincible(normalInvincible.isSelected());
				}
			}
		});
		p2.add(normalInvincible);
		grabInvincible = new JCheckBox("Grab Invincible");
		grabInvincible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!steps.isSelectionEmpty()) {
					getSelectedStep().setHitInvincible(grabInvincible.isSelected());
				}
			}
		});
		p2.add(grabInvincible);
		projectileInvincible = new JCheckBox("Projectile Invincible");
		projectileInvincible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!steps.isSelectionEmpty()) {
					getSelectedStep().setProjectileInvincible(projectileInvincible.isSelected());
				}
			}
		});
		p2.add(projectileInvincible);
		ignoresGravity = new JCheckBox("Ignores Gravity");
		ignoresGravity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!steps.isSelectionEmpty()) {
					getSelectedStep().setIgnoresGravity(ignoresGravity.isSelected());
				}
			}
		});
		p2.add(ignoresGravity);
		setVelocity = new JCheckBox("Sets fighter velocity");
		setVelocity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!steps.isSelectionEmpty()) {
					getSelectedStep().setSetVelocity(setVelocity.isSelected());
				}
			}
		});
		p2.add(setVelocity);
		velocityX = new JSpinner(new SpinnerNumberModel(0d, -100d, 100d, 0.25d));
		velocityX.setToolTipText("X Velocity");
		velocityX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!steps.isSelectionEmpty()) {
					Double f = (Double) velocityX.getValue();
					getSelectedStep().getVelocity().setX(f.floatValue());
				}
			}
		});
		p2.add(velocityX);
		velocityY = new JSpinner(new SpinnerNumberModel(0d, -100d, 100d, 0.25d));
		velocityY.setToolTipText("Y Velocity");
		velocityY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!steps.isSelectionEmpty()) {
					Double f = (Double) velocityY.getValue();
					getSelectedStep().getVelocity().setY(f.floatValue());
				}
			}
		});
		p2.add(velocityY);
		stepPanel.add(p2, BorderLayout.SOUTH);
		frame.getContentPane().add(stepPanel);
		
		cBoxesPanel = new JPanel(new BorderLayout());
		boxModel = new DefaultListModel<>();
		cBoxes = new JList<>(boxModel);
		cBoxes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cBoxes.addListSelectionListener(new ListSelectionListener() {
            @Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					if (cBoxes.isSelectionEmpty()) {
						resetBoxVals();
					} else {
						selectBox(getSelectedBox());
					}
				}
			}
		});
		cBoxesScroller = new JScrollPane(cBoxes);
		cBoxesPanel.add(cBoxesScroller, BorderLayout.CENTER);
		JPanel j3 = new JPanel(new GridLayout(0, 2));
		addBox = new JButton("+");
		addBox.setToolTipText("Add collision box");
		addBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (steps.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(frame, "You need to select an animation step first!");
					return;
				}
				String name = JOptionPane.showInputDialog(frame, "Name of hitbox");
				if (name == null) {
					return;
				}
				Double f1 = (Double) trajectoryX.getValue();
				Double f2 = (Double) trajectoryY.getValue();
				steps.getSelectedValue().getCollisions().getBoxes()
						.add(new CollisionBox(name, Color.YELLOW, boxX.getValue(), boxY.getValue(), boxW.getValue(),
								boxH.getValue(), rotation.getValue(), false, (int) damage.getValue(),
								(int) hitstun.getValue(), knockdown.isSelected(),
								new Vector(f1.floatValue(), f2.floatValue())));
				boxModel.addElement(steps.getSelectedValue().getCollisions().getBoxes()
						.get(steps.getSelectedValue().getCollisions().getBoxes().size() - 1));
				cBoxes.setSelectedIndex(boxModel.size() - 1);
				selectBox(cBoxes.getSelectedValue());
			}
		});
		j3.add(addBox);
		deleteBox = new JButton("-");
		deleteBox.setToolTipText("Delete collision box");
		deleteBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					int newSelection = cBoxes.getSelectedIndex() - 1;
					anims.getSelectedValue().getSteps().get(steps.getSelectedIndex()).getCollisions().getBoxes()
							.remove(cBoxes.getSelectedIndex());
					boxModel.remove(cBoxes.getSelectedIndex());
					if (boxModel.size() > 0) {
						if (newSelection < 0) {
							newSelection = 0;
						}
						cBoxes.setSelectedValue(boxModel.getElementAt(newSelection), true);
						selectBox(boxModel.getElementAt(newSelection));
					} else {
						resetBoxVals();
					}
				}
			}
		});
		j3.add(deleteBox);
		/*hitbox = new JCheckBox("Hitbox");
		hitbox.setToolTipText("Hitbox or Hurtbox");
		j3.add(hitbox);*/
		boxName = new JTextField("Name");
		boxName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					getSelectedBox().setName(boxName.getText());
					cBoxes.repaint();
				}
			}
		});
		j3.add(boxName);
		color = new JButton("Color");
		color.setToolTipText("Color of box");
		color.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					Color newColor = JColorChooser.showDialog(
		                     frame, "Choose Color", currColor);
					if (newColor != null) {
						currColor = newColor;
						getSelectedBox().setColor(currColor);
					}
				}
			}
		});
		j3.add(color);
		boxX = new JSlider(0, 400, 110); //new JSpinner(new SpinnerNumberModel(50, 0, 1000, 1));
		boxX.setToolTipText("X Location");
		boxX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					getSelectedBox().setX(boxX.getValue()); 
				}
			}
		});
		j3.add(boxX);
		boxY = new JSlider(0, 400, 85); //new JSpinner(new SpinnerNumberModel(50, 0, 1000, 1));
		boxY.setToolTipText("Y Location");
		boxY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					getSelectedBox().setY(boxY.getValue()); 
				}
			}
		});
		j3.add(boxY);
		boxW = new JSlider(0, 400, 60); //new JSpinner(new SpinnerNumberModel(75, 0, 1000, 1));
		boxW.setToolTipText("Width");
		boxW.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					getSelectedBox().setWidth(boxW.getValue()); 
				}
			}
		});
		j3.add(boxW);
		boxH = new JSlider(0, 400, 25); //new JSpinner(new SpinnerNumberModel(25, 0, 1000, 1));
		boxH.setToolTipText("Height");
		boxH.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					getSelectedBox().setHeight(boxH.getValue());
				}
			}
		});
		j3.add(boxH);
		rotation = new JSlider(0, 360, 0);//(new SpinnerNumberModel(0, 0, 359, 1));
		rotation.setToolTipText("Rotation (degrees)");
		rotation.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					getSelectedBox().setAngle(rotation.getValue()); 
				}
			}
		});
		j3.add(rotation);
		damage = new JSpinner(new SpinnerNumberModel(-1, -1, 1000, 1));
		damage.setToolTipText("Damage hitbox does");
		damage.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					getSelectedBox().setDamage((int) damage.getValue());
				}
			}
		});
		j3.add(damage);
		trajectoryX = new JSpinner(new SpinnerNumberModel(0d, -25d, 25d, 0.25d));
		trajectoryX.setToolTipText("Trajectory (X Component)");
		trajectoryX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					Double f = (Double) trajectoryX.getValue();
					getSelectedBox().getTrajectory(false).setX(f.floatValue()); 
				}
			}
		});
		j3.add(trajectoryX);
		trajectoryY = new JSpinner(new SpinnerNumberModel(0d, -25d, 25d, 0.25d));
		trajectoryY.setToolTipText("Trajectory (Y Component)");
		trajectoryY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					Double f = (Double) trajectoryY.getValue();
					getSelectedBox().getTrajectory(false).setY(f.floatValue()); 
				}
			}
		});
		j3.add(trajectoryY);
		knockdown = new JCheckBox("Knocks down");
		knockdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					getSelectedBox().setKnockdown(knockdown.isSelected());
				}
			}
		});
		j3.add(knockdown);
		hitstun = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
		hitstun.setToolTipText("Frames of hitstun");
		hitstun.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					getSelectedBox().setHitstunFrames((int) hitstun.getValue());
				}
			}
		});
		j3.add(hitstun);
		cBoxesPanel.add(j3, BorderLayout.SOUTH);
		frame.getContentPane().add(cBoxesPanel);
		
		previewWindow = new JPanel() {			
			@Override
			public void paintComponent(Graphics g) {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());
				if (!animModel.isEmpty() && !anims.isSelectionEmpty()
						&& !anims.getSelectedValue().getSteps().isEmpty()) {
					if (preview.isSelected()) {
						anims.getSelectedValue().draw(0, 0, g, false);
						anims.getSelectedValue().advance();
					} else if (!steps.isSelectionEmpty()){
						steps.getSelectedValue().draw(0, 0, g, false);
					}
					g.setColor(new Color(50, 50, 50, 150));
					g.fillRect(0, anims.getSelectedValue().getGroundLocation(), getWidth(),
							1000);
					g.setColor(new Color(50, 50, 50, 200));
					g.drawRect(-1, anims.getSelectedValue().getGroundLocation(), getWidth(),
							1000);
				}
			}
		};
		frame.getContentPane().add(previewWindow);
		frame.setPreferredSize(new Dimension(1000, 400));
		frame.pack();
		new Thread(new Runnable() {
			long lastDraw;
			
			@Override
			public void run() {
				while (true) {
					if (System.currentTimeMillis() - lastDraw > (1000 / Window.FRAMES_PER_SECOND)) {
						lastDraw = System.currentTimeMillis();
						previewWindow.repaint();
					}
				}
			}
			
		}).start();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static AnimationStep getSelectedStep() {
		return anims.getSelectedValue().getSteps().get(steps.getSelectedIndex());
	}
	
	public static CollisionBox getSelectedBox() {
		return anims.getSelectedValue().getSteps().get(steps.getSelectedIndex()).getCollisions().getBoxes()
				.get(cBoxes.getSelectedIndex());
	}
	
	public static void resetAnimVals() {
		name.setText("Name");
		repeat.setSelected(false);
		groundLocation.setValue(200);
		stepModel.clear();
		resetStepVals();
	}
	
	public static void resetStepVals() {
		interuptable.setSelected(false);
		specialCancelable.setSelected(false);
		frameCount.setValue(Animation.FRAMES_PER_SPRITE);
		hitInvincible.setSelected(false);
		normalInvincible.setSelected(false);
		grabInvincible.setSelected(false);
		projectileInvincible.setSelected(false);
		armorAmount.setValue(0);
		setVelocity.setSelected(false);
		ignoresGravity.setSelected(false);
		velocityX.setValue(0d);
		velocityY.setValue(0d);
		boxModel.clear();
		resetBoxVals();
	}
	
	public static void resetBoxVals() {
		boxName.setText("Name");
		rotation.setValue(0);
		boxX.setValue(50);
		boxY.setValue(50);
		boxW.setValue(75);
		boxH.setValue(25);
		damage.setValue(0);
		trajectoryX.setValue(0d);
		trajectoryY.setValue(0d);
		hitstun.setValue(0);
		knockdown.setSelected(false);
	}
	
	public static void selectAnim(Animation anim) {
		name.setText(anim.toString());
		repeat.setSelected(anim.repeat());
		groundLocation.setValue(anim.getGroundLocation());
		
		stepModel.clear();
		for (AnimationStep s : anim.getSteps()) {
			stepModel.addElement(s);
		}
		if (!stepModel.isEmpty()) {
			steps.setSelectedIndex(0);
			selectStep(stepModel.getElementAt(0));
		}
	}
	
	public static void selectStep(AnimationStep step) {
		interuptable.setSelected(step.isInteruptable());
		specialCancelable.setSelected(step.isSpecialInteruptable());
		frameCount.setValue(step.getFramesToDisplay());
		hitInvincible.setSelected(step.isHitInvincible());
		normalInvincible.setSelected(step.isNormalInvincible());
		grabInvincible.setSelected(step.isGrabInvincible());
		projectileInvincible.setSelected(step.isProjectileInvincible());
		armorAmount.setValue(step.getArmorAmount());
		ignoresGravity.setSelected(step.isIgnoresGravity());
		setVelocity.setSelected(step.setsVelocity());
		velocityX.setValue((double) step.getVelocity().getX());
		velocityY.setValue((double) step.getVelocity().getY());
		
		boxModel.clear();
		for (CollisionBox c : step.getCollisions().getBoxes()) {
			boxModel.addElement(c);
		}
		if (!boxModel.isEmpty()) {
			cBoxes.setSelectedIndex(0);
			selectBox(boxModel.getElementAt(0));
		}
	}
	
	public static void selectBox(CollisionBox box) {
		boxName.setText(box.toString());
		rotation.setValue((int) box.getAngle());
		boxX.setValue(box.getX());
		boxY.setValue(box.getY());
		boxW.setValue(box.getWidth());
		boxH.setValue(box.getHeight());
		damage.setValue(box.getDamage());
		trajectoryX.setValue((double) box.getTrajectory(false).getX());
		trajectoryY.setValue((double) box.getTrajectory(false).getY());
		hitstun.setValue(box.getHitstunFrames());
		knockdown.setSelected(box.knocksDown());
	}

	public static void saveAnimations(File f) {
		try(BufferedWriter writer = Files.newBufferedWriter(
		        f.toPath(), Charset.defaultCharset())){
			String tab = "	";
			for (int i = 0; i < animModel.size(); i++) {
				Animation a = animModel.getElementAt(i);
				writer.append("Animation: \"" + a.toString() + "\" {");
				writer.newLine();
				writer.append(tab + "Repeat: " + a.repeat());
				writer.newLine();
				writer.append(tab + "GroundLocation: " + a.getGroundLocation());
				writer.newLine();
				writer.append(tab + "Steps[" + a.getSteps().size() + "] {");
				writer.newLine();
				for (int n = 0; n < a.getSteps().size(); n++) {
					AnimationStep step = a.getSteps().get(n);
					ByteArrayOutputStream baos=new ByteArrayOutputStream(1000);
					ImageIO.write(step.getSprite().getImage(), "png", baos);
					baos.flush();
					String bytes = Base64.encode(baos.toByteArray());
					baos.close();
					writer.append(tab + tab + "[Step " + n + "] {");
					writer.newLine();
					writer.append(tab + tab + tab + "Image: " +  bytes);
					writer.newLine();
					writer.append(tab + tab + tab + "FramesToDisplay: " + step.getFramesToDisplay());
					writer.newLine();
					writer.append(tab + tab + tab + "Interuptable: " + step.isInteruptable());
					writer.newLine();
					writer.append(tab + tab + tab + "SpecialCancelable: " + step.isSpecialInteruptable());
					writer.newLine();
					writer.append(tab + tab + tab + "HitInvincible: " + step.isHitInvincible());
					writer.newLine();
					writer.append(tab + tab + tab + "NormalInvincible: " + step.isNormalInvincible());
					writer.newLine();
					writer.append(tab + tab + tab + "GrabInvincible: " + step.isGrabInvincible());
					writer.newLine();
					writer.append(tab + tab + tab + "ProjectileInvincible: " + step.isProjectileInvincible());
					writer.newLine();
					writer.append(tab + tab + tab + "ArmorAmount: " + step.getArmorAmount());
					writer.newLine();
					writer.append(tab + tab + tab + "IgnoresGravity: " + step.isIgnoresGravity());
					writer.newLine();
					writer.append(tab + tab + tab + "Velocity: " + step.getVelocity());
					writer.newLine();
					writer.append(tab + tab + tab + "SetVelocity: " + step.setsVelocity());
					writer.newLine();
					writer.append(tab + tab + tab + "CollisionBoxes[" + step.getCollisions().getBoxes().size() + "] {");
					writer.newLine();
					for (int m = 0; m < step.getCollisions().getBoxes().size(); m++) {
						CollisionBox box = step.getCollisions().getBoxes().get(m);
						writer.append(tab + tab + tab + tab + "[" + box.toString() + "] {");
						writer.newLine();
						writer.append(tab + tab + tab + tab + tab + "Color: " + box.getColor().getRGB());
						writer.newLine();
						writer.append(tab + tab + tab + tab + tab + "X: " + box.getX());
						writer.newLine();
						writer.append(tab + tab + tab + tab + tab + "Y: " + box.getY());
						writer.newLine();
						writer.append(tab + tab + tab + tab + tab + "Width: " + box.getWidth());
						writer.newLine();
						writer.append(tab + tab + tab + tab + tab + "Height: " + box.getHeight());
						writer.newLine();
						writer.append(tab + tab + tab + tab + tab + "Rotation: " + box.getAngle());
						writer.newLine();
						writer.append(tab + tab + tab + tab + tab + "Damage: " + box.getDamage());
						writer.newLine();
						writer.append(tab + tab + tab + tab + tab + "Hitstun: " + box.getHitstunFrames());
						writer.newLine();
						writer.append(tab + tab + tab + tab + tab + "Knockdown: " + box.knocksDown());
						writer.newLine();
						writer.append(tab + tab + tab + tab + tab + "Trajectory: " + box.getTrajectory(false));
						writer.newLine();
						writer.append(tab + tab + tab + tab + "}");
						writer.newLine();
					}
					writer.append(tab + tab + tab + "}");
					writer.newLine();
					writer.append(tab + tab + "}");
					writer.newLine();
				}
				writer.append(tab + "}");
				writer.newLine();
				writer.append("}");
				writer.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
